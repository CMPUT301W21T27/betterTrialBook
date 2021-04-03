package com.example.bettertrialbook.statistic;

import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class HistogramInfo {

    public HistogramInfo() {
        // Empty Constructor
    }

    public ArrayList<Integer> collectFrequency(ArrayList<Double> data) {
        int requiredBin;
        int diffForBin;
        Double diffForBins;
        Double range = rangeOfData(data);
        ArrayList<Integer> binFrequency = new ArrayList<>();

        // Get the number of Bins to group the dataSet
        requiredBin = getNumberofBins(data);

        // Calculate the difference between each neighbor bins.
        diffForBins = range / requiredBin;
        diffForBin = diffForBins.intValue();

        // Set up the maximum value for each group
        int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
        int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, requiredBin);

        // Compare each value to the maximum with its respective minimum
        int frequency;
        for (int i = 0; i < requiredBin; i++) {
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

    public ArrayList<String> getLabels(ArrayList<Double> data) {
        int requiredBin = getNumberofBins(data);
        Double diffForBins = rangeOfData(data) / getNumberofBins(data);
        int diffForBin = diffForBins.intValue();
        int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
        int[] minForEachBin = getMinForEachBin(diffForBin, maxForEachBin, requiredBin);

        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < requiredBin; i++) {
            String min = String.valueOf(minForEachBin[i]);
            String max = String.valueOf(maxForEachBin[i]);
            String label = min + '-' + max;
            labels.add(label);
        }

        return labels;
    }

    //------------------------------------Helper Method Below---------------------------------------
    public Double rangeOfData(ArrayList<Double> data) {
        return ( data.get(data.size() - 1) - data.get(0) );
    }

    public int getNumberofBins(ArrayList<Double> data) {
        return Math.min(distinctExperimentData(data).size(), 5);
    }

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

    // Write Down the Credit later on
    public ArrayList<Double> distinctExperimentData(ArrayList<Double> data) {
        Set<Double> set = new LinkedHashSet<>();
        ArrayList<Double> distinctItem = new ArrayList<>();

        set.addAll(data);
        distinctItem.addAll(set);
        Collections.sort(distinctItem);

        return distinctItem;
    }
}

