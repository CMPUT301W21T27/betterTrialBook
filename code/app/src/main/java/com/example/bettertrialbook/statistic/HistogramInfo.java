package com.example.bettertrialbook.statistic;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class HistogramInfo {

    /**
     *  An Constructor to initialize the related histogram setting methods
     */
    public HistogramInfo() { /*Empty Constructor*/}


    /**
     * Find the frequency for each respective bins
     * @param data
     * An sorted arrayList contains the data value for each trials
     * @return
     * An arrayList that represents the number frequency of respective bin
     * in non-decreasing order.
     */
    public ArrayList<Integer> collectFrequency(ArrayList<Double> data) {
        int requiredBin;
        ArrayList<Integer> binFrequency = new ArrayList<>();

        if (data != null && data.size() > 0) {
            // Get the number of Bins to group the dataSet
            requiredBin = getNumberofBins(data);

            // Get the frequency for each bin depends on the requiredBin
            if (requiredBin >= 5) {
                binFrequency = getBinFrequencyA(data, requiredBin);
            }
            else if (requiredBin > 0 && requiredBin < 5) {
                binFrequency = getBinFrequencyB(data, requiredBin);
            }
        } else {
            binFrequency = null;
        }

        return binFrequency;
    }


    /**
     * Obtain the name for the bins in the X-Axis for the Histogram
     * @param data
     * An sorted arrayList contains the data value for each trials
     * @return
     * An arrayList contains the labels( The name for the bin : String ) for each bins
     */
    public ArrayList<String> getLabels(ArrayList<Double> data) {
        ArrayList<String> labels = new ArrayList<>();

        if (data != null && data.size() > 0) {
            int requiredBin = getNumberofBins(data);

            // When the required Bin is larger or equal to 5, get the range as the name for the bin
            // When the required Bin is smaller than 5, get the exact element as the name for the bin
            if (requiredBin >= 5) {
                Double diffForBins = rangeOfData(data) / getNumberofBins(data);
                int diffForBin = diffForBins.intValue();
                // maxForEachBin[i] and minForEachBin[i] forms a range for a bin
                int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
                int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, requiredBin);

                for (int i = 0; i < requiredBin; i++) {
                    // For the last bin, it includes all the numbers above the mean
                    // For the regular bin, its range is from min to max.
                    if (i == requiredBin - 1) {
                        String min = String.valueOf(minForEachBin[i]);
                        String label = min + "+";
                        labels.add(label);
                    } else {
                        String min = String.valueOf(minForEachBin[i]);
                        String max = String.valueOf(maxForEachBin[i]);
                        String label = min + '-' + max;
                        labels.add(label);
                    }
                }
            }
            // Used the elements as the name for the bin
            else if (requiredBin > 0 && requiredBin < 5){
                double[] categoryBin = getCategory(data, requiredBin);
                for (int i = 0; i < requiredBin; i++) {
                    labels.add(String.valueOf(categoryBin[i]));
                }
            }
        } else {
            labels = null;
        }

        return labels;
    }

    //------------------------------------Helper Method Below---------------------------------------
    // Obtain an arrayList of distinct elements from the trials' data of the experiment
    public ArrayList<Double> distinctExperimentData(ArrayList<Double> data) {
        Set<Double> set = new LinkedHashSet<>();
        ArrayList<Double> distinctItem = new ArrayList<>();

        // Linked Hash List will only accept one time for the elements.
        // Duplicated elements will be "thrown away".
        set.addAll(data);
        distinctItem.addAll(set);
        Collections.sort(distinctItem);

        return distinctItem;
    }

    // Obtain the range of the data
    public Double rangeOfData(ArrayList<Double> data) {
        // Logic: Since the data passed in is a sorted arrayList
        // The first element in the array list is the smallest element
        // The last element in the array list is the largest element
        return (data.get(data.size() - 1) - data.get(0));
    }

    // Obtain the number of bins used to create the histogram ( 0-5 )
    public int getNumberofBins(ArrayList<Double> data) {
        return Math.min(distinctExperimentData(data).size(), 5);
    }

    // The below methods are used for more than 5 distinct elements
    // Calculate the range of the bin
    public int rangeForBins(Double range, int requiredBin) {
        Double rangeForBins;

        rangeForBins = range / requiredBin;

        return rangeForBins.intValue();
    }

    // Calculate the maximum value for each bin
    public int[] getMaxForEachBin(int diffForBin, int requiredBin) {
        int[] maxForEachBin = new int[requiredBin];

        maxForEachBin[0] = diffForBin;
        for (int i = 1; i < requiredBin; i++) {
            maxForEachBin[i] = maxForEachBin[i-1] + 1 + diffForBin;
        }

        return maxForEachBin;
    }

    // Calculate the minimum value for each bin
    public int[] getMinForEachBin(int diffForBin, int[] maxForEachBin, int requiredBin) {
        int[] minForEachBin = new int[requiredBin];

        minForEachBin[0] = 0;
        for (int i = 1; i < requiredBin; i++) {
            minForEachBin[i] = maxForEachBin[i-1] + 1;
        }

        return minForEachBin;
    }

    public ArrayList<Integer> getBinFrequencyA(ArrayList<Double> data, int noOfBin) {
        ArrayList<Integer> binFrequency = new ArrayList<>();

        double range = rangeOfData(data);

        Double diffForBins = range / noOfBin;
        int diffForBin = diffForBins.intValue();

        int[] maxForEachBin = getMaxForEachBin(diffForBin, noOfBin);
        int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, noOfBin);

        int frequency;
        for (int i = 0; i < noOfBin; i++) {
            frequency = 0;
            for (int j = 0; j < data.size(); j++) {
                // The last bin, only check if the value is larger than the minForEachBin[i] value
                // Regular bins, check if the value is larger than the minForEachBin[i] value and
                // smaller than maxForEachBin[i] value
                if (i == noOfBin - 1) {
                    if (data.get(j) >= minForEachBin[i]) {
                        frequency += 1;
                    }
                } else {
                    if (data.get(j) >= minForEachBin[i] && data.get(j) <= maxForEachBin[i]) {
                        frequency += 1;
                    }
                }
            }

            binFrequency.add(frequency);
        }

        return binFrequency;
    }
    // end for methods used for larger than or equal to 5 distinct elements


    // The below method is used for less than 5 distinct elements
    // Obtain the labels for the histogram, directly use the element as the name
    public double[] getCategory(ArrayList<Double> data, int requiredBin) {
        double[] category = new double[requiredBin];

        ArrayList<Double> distinctItem = distinctExperimentData(data);
        for (int i = 0; i < requiredBin; i++) {
            category[i] = distinctItem.get(i);
        }

        return category;
    }

    public ArrayList<Integer> getBinFrequencyB(ArrayList<Double> data, int noOfBin) {
        double[] category;
        ArrayList<Integer> binFrequency = new ArrayList<>();

        category = getCategory(data, noOfBin);

        int frequency;
        for (int i = 0; i < noOfBin; i++) {
            frequency = 0;
            for (Double value : data) {
                if (value == category[i]) {
                    frequency += 1;
                }
            }
            binFrequency.add(frequency);
        }

        return binFrequency;
    }
    // end for methods used for less than 5 distinct elements
}

