package com.example.bettertrialbook.statistic;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.Trial;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class HistogramInfo {

    private String experimentType;
    private ArrayList<Trial> trials;
    private ArrayList<Double> experimentData;
    private Statistic statistic = new Statistic();

    /**
     * An constructor to initialize the methods related to the Histogram
     * @param trials
     * The trial data for the experiment
     * @param experimentType
     * The type of the experiment
     */
    public HistogramInfo(ArrayList<Trial> trials, String experimentType) {
        this.trials = trials;
        this.experimentType = experimentType;
        this.experimentData = statistic.experimentData(this.trials);
    }

    /**
     * Finds the frequency for each respective bin
     * @return
     * An arrayList that represents the number frequency of respective bin
     * in non-decreasing order.
     */
    public ArrayList<Integer> collectFrequency() {
        int requiredBin;
        ArrayList<Integer> binFrequency = new ArrayList<>();

        if (experimentData != null && experimentData.size() > 0) {
            // Get the number of Bins to group the dataSet
            requiredBin = getNumberofBins();

            // Get the frequency for each bin depends on the requiredBin
            if (requiredBin >= 5) {
                binFrequency = getBinFrequencyA(requiredBin);
            } else if (requiredBin > 0 && requiredBin < 5) {
                binFrequency = getBinFrequencyB(requiredBin);
            }
        } else {
            binFrequency = null;
        }

        return binFrequency;
    }

    /**
     * Obtain the name for the bins in the X-Axis for the Histogram
     * @return An arrayList contains the labels( The name for the bin : String ) for each bins
     */
    public ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();

        if (experimentData != null && experimentData.size() > 0) {
            int requiredBin = getNumberofBins();

            // When the required Bin is larger or equal to 5, get the range as the name for the bin
            // When the required Bin is smaller than 5, get the exact element as the name for the bin
            if (requiredBin >= 5) {
                Double diffForBins = rangeOfData() / requiredBin;
                int diffForBin = diffForBins.intValue();
                // maxForEachBin[i] and minForEachBin[i] forms a range for a bin
                int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
                int[] minForEachBin = getMinForEachBin(maxForEachBin, requiredBin);

                // Depends on the experiment Type, there are different ways to create the labels.
                if (experimentType.equals(Extras.COUNT_TYPE)) {
                    labels.add("Count");
                } else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                    labels.add("Failure");
                    labels.add("Success");
                } else {
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
            }
            else if (requiredBin > 0 && requiredBin < 5) {
                double[] categoryBin = getCategory(requiredBin);
                if (experimentType.equals(Extras.COUNT_TYPE)) {
                    labels.add("Count");
                } else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                    labels.add("Failure");
                    labels.add("Success");
                }
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
    /**
     * Obtain the distinct value in the experiment
     * @return
     * An arrayList of distinct value in the experiment in ascending order
     */
    public ArrayList<Double> distinctExperimentData() {
        Set<Double> set = new LinkedHashSet<>();
        ArrayList<Double> distinctItem = new ArrayList<>();

        // Linked Hash List will only accept one time for the elements.
        // Duplicated elements will be "thrown away".
        set.addAll(experimentData);
        distinctItem.addAll(set);
        Collections.sort(distinctItem);

        return distinctItem;
    }

    /**
     *
     * @return
     * The difference between the max value and the min value
     */
    public Double rangeOfData() {
        // Logic: Since the data passed in is a sorted arrayList
        // The first element in the array list is the smallest element
        // The last element in the array list is the largest element
        return (experimentData.get(experimentData.size() - 1) - experimentData.get(0));
    }

    /**
     *
     * @return
     * The number of bins required to create the histogram (Max. 5)
     */
    public int getNumberofBins() {
        return Math.min(distinctExperimentData().size(), 5);
    }

    /**
     * Caluclate the maximum value for each bin
     * @param diffForBin
     * The difference within the bin
     * @param requiredBin
     * The number of bins we are going to created for the Histogram
     * @return
     * Return the list of integer contains the maximum value for each bin
     * in ascending order.
     */
    public int[] getMaxForEachBin(int diffForBin, int requiredBin) {
        int[] maxForEachBin = new int[requiredBin];

        if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                if (diffForBin == 0) {
                    diffForBin = 1;
                }
                maxForEachBin[0] = diffForBin;
                for (int i = 1; i < requiredBin; i++) {
                    maxForEachBin[i] = maxForEachBin[i - 1] + diffForBin;
                }
        } else {
            maxForEachBin[0] = diffForBin;
            for (int i = 1; i < requiredBin; i++) {
                maxForEachBin[i] = maxForEachBin[i - 1] + 1 + diffForBin;
            }
        }

        return maxForEachBin;
    }

    /**
     * Calculate the minimum value for each bin
     * @param maxForEachBin
     * A list of integer contains the maximum value for each bin
     * @param requiredBin
     * The number of bins we are going to created for the Histogram
     * @return
     * Return the list of integer contains the minimum value for each bin
     * in ascending order.
     */
    public int[] getMinForEachBin(int[] maxForEachBin, int requiredBin) {
        int[] minForEachBin = new int[requiredBin];
        minForEachBin[0] = 0;

        if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
            for (int i = 1; i < requiredBin; i++) {
                minForEachBin[i] = maxForEachBin[i - 1];
            }
        } else {
            minForEachBin[0] = 0;
            for (int i = 1; i < requiredBin; i++) {
                minForEachBin[i] = maxForEachBin[i - 1] + 1;
            }
        }

        return minForEachBin;
    }

    /**
     * Collect the frequency for each bins (At least equal or more than 5 bins)
     * @param noOfBin
     * The number of bins we are going to created for the Histogram
     * @return
     * Return the arrayList of integer contains the frequency for each bin
     * in ascending order.
     */
    public ArrayList<Integer> getBinFrequencyA(int noOfBin) {
        ArrayList<Integer> binFrequency = new ArrayList<>();

        double range = rangeOfData();
        Double diffForBins = range / noOfBin;
        int diffForBin = diffForBins.intValue();

        int[] maxForEachBin = getMaxForEachBin(diffForBin, noOfBin);
        int[] minForEachBin = getMinForEachBin(maxForEachBin, noOfBin);

        int frequency;
        for (int i = 0; i < noOfBin; i++) {
            frequency = 0;
            for (int j = 0; j < experimentData.size(); j++) {
                // The last bin, only check if the value is larger than the minForEachBin[i] value
                // Regular bins, check if the value is larger than the minForEachBin[i] value and
                // smaller than maxForEachBin[i] value
                if (i == noOfBin - 1) {
                    if (experimentData.get(j) >= minForEachBin[i]) {
                        frequency += 1;
                    }
                } else {
                    if (experimentData.get(j) >= minForEachBin[i] && experimentData.get(j) <= maxForEachBin[i]) {
                        frequency += 1;
                    }
                }
            }
            binFrequency.add(frequency);
        }

        return binFrequency;
    }

    /**
     * Obtain the category (label) for each bin which are used to plot the histogram
     * @param requiredBin
     * The number of bins we are going to created for the Histogram
     * @return
     * An list of double value respesents as the label for the x-axis on the histogram
     */
    public double[] getCategory(int requiredBin) {
        double[] category = new double[requiredBin];

        ArrayList<Double> distinctItem = distinctExperimentData();
        for (int i = 0; i < requiredBin; i++) {
            category[i] = distinctItem.get(i);
        }

        return category;
    }


    /**
     * Collect the frequency for each bins (Less than 5 bins)
     * @param noOfBin
     * The number of bins we are going to created for the Histogram
     * @return
     * Return the arrayList of integer contains the frequency for each bin
     * in ascending order.
     */
    public ArrayList<Integer> getBinFrequencyB(int noOfBin) {
        double[] category;
        ArrayList<Integer> binFrequency = new ArrayList<>();

        category = getCategory(noOfBin);

        int frequency;
        for (int i = 0; i < noOfBin; i++) {
            frequency = 0;
            for (Double value : experimentData) {
                if (value == category[i]) {
                    frequency += 1;
                }
            }
            binFrequency.add(frequency);
        }

        return binFrequency;
    }
}