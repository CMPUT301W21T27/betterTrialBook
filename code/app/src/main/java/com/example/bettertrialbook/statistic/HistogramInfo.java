package com.example.bettertrialbook.statistic;

import android.os.Parcelable;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class HistogramInfo {

    /**
     *  An Constructor to initialize the related histogram setting methods
     */
    public HistogramInfo() { /*Empty Constructor*/}

    public ArrayList<Integer> collectFrequency(ArrayList<Double> data) {
        int requiredBin;
        int diffForBin;
        Double diffForBins;
        ArrayList<Integer> binFrequency = new ArrayList<>();

        // Get the number of Bins to group the dataSet
        requiredBin = getNumberofBins(data);

        if (requiredBin >= 5) {
            binFrequency = getBinFrequencyA(data, requiredBin);
        } else {
            binFrequency = getBinFrequencyB(data, requiredBin);
        }

        return binFrequency;
    }

    public ArrayList<String> getLabels(ArrayList<Double> data) {
        int requiredBin = getNumberofBins(data);
        ArrayList<String> labels = new ArrayList<>();

        if (requiredBin >= 5) {
            Double diffForBins = rangeOfData(data) / getNumberofBins(data);
            int diffForBin = diffForBins.intValue();
            int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
            int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, requiredBin);

            for (int i = 0; i < requiredBin; i++) {
                String min = String.valueOf(minForEachBin[i]);
                String max = String.valueOf(maxForEachBin[i]);
                String label = min + '-' + max;
                labels.add(label);
            }
        } else {
            double[] categoryBin = getCategory(data, requiredBin);
            for (int i = 0; i < requiredBin; i++) {
                labels.add(String.valueOf(categoryBin[i]));
            }
        }

        return labels;
    }

    //------------------------------------Helper Method Below---------------------------------------
    public ArrayList<Double> distinctExperimentData(ArrayList<Double> data) {
        Set<Double> set = new LinkedHashSet<>();
        ArrayList<Double> distinctItem = new ArrayList<>();

        set.addAll(data);
        distinctItem.addAll(set);
        Collections.sort(distinctItem);

        return distinctItem;
    }

    public Double rangeOfData(ArrayList<Double> data) {
        return ( data.get(data.size() - 1) - data.get(0) );
    }

    public int getNumberofBins(ArrayList<Double> data) {
        return Math.min(distinctExperimentData(data).size(), 5);
    }

    // For more than 5 distinct elements
    public int differenceForBins(Double range, int requiredBin) {
        Double diffForBins;

        diffForBins = range / requiredBin;

        return diffForBins.intValue();
    }

    public int[] getMaxForEachBin(int diffForBin, int requiredBin) {
        int[] maxForEachBin = new int[requiredBin];

        maxForEachBin[0] = diffForBin;
        for (int i = 1; i < requiredBin; i++) {
            maxForEachBin[i] = maxForEachBin[i-1] + 1 + diffForBin;
        }

        return maxForEachBin;
    }

    public int[] getMinForEachBin(int diffForBin, int[] maxForEachBin, int requiredBin) {
        int[] minForEachBin = new int[requiredBin];

        minForEachBin[0] = 0;
        for (int i = 1; i < requiredBin; i++) {
            minForEachBin[i] = maxForEachBin[i-1] + 1;
        }

        return minForEachBin;
    }

    public ArrayList<Integer> getBinFrequencyA(ArrayList<Double> data, int noOfBin) {
        double range = rangeOfData(data);
        ArrayList<Integer> binFrequency = new ArrayList<>();

        Double diffForBins = range / noOfBin;
        int diffForBin = diffForBins.intValue();

        int[] maxForEachBin = getMaxForEachBin(diffForBin, noOfBin);
        int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, noOfBin);

        int frequency;
        for (int i = 0; i < noOfBin; i++) {
            frequency = 0;
            for (Double value : data) {
                if (value >= minForEachBin[i] && value <= maxForEachBin[i]) {
                    frequency += 1;
                }
            }
            binFrequency.add(frequency);
        }

        return binFrequency;
    }

    // For less than 5 distinct elements;
    public double[] getCategory(ArrayList<Double> data, int requiredBin) {
        double[] category = new double[requiredBin];
        ArrayList<Double> distinctItem = distinctExperimentData(data);

        for (int i = 0; i < requiredBin; i++) {
            category[i] = distinctItem.get(i);
        }

        return category;
    }

    public ArrayList<Integer> getBinFrequencyB(ArrayList<Double> data, int noOfBin) {
        double[] category = new double[noOfBin];
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
}

