package com.example.bettertrialbook.statistic;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * A model to to hold relevant data for the statistics and histogram graph
 */
public class HistogramInfo {
    private Statistic statistic;
    private String experimentType;
    private ArrayList<Trial> trials;
    private ArrayList<Double> experimentData;

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
        this.statistic = new Statistic();
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
            requiredBin = Math.min(distinctExperimentData().size(), 5);

            // Get the frequency for each bin depends on the requiredBin
            if (requiredBin >= 5) {
                // range style of bin method
                binFrequency = getBinFrequencyA(requiredBin);
            } else if (requiredBin > 0 && requiredBin < 5) {
                // categorize style of bin method
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
        DecimalFormat df = new DecimalFormat("#.##");

        if (experimentData != null && experimentData.size() > 0) {
            int requiredBin = Math.min(distinctExperimentData().size(), 5);

            // Depends on the experiment Type, there are different ways to create the labels.
            // When the required Bin is larger or equal to 5, get the range as the name for the bin
            if (requiredBin >= 5) {
                Double diffForBins = rangeOfData() / requiredBin;
                int diffForBin = diffForBins.intValue();
                // maxForEachBin[i] and minForEachBin[i] forms a range for a bin
                int[] maxForEachBin = getMaxForEachBin(diffForBin, requiredBin);
                int[] minForEachBin = getMinForEachBin(maxForEachBin, requiredBin);

                if (experimentType.equals(Extras.COUNT_TYPE)) {
                    labels.add("Count");
                } else if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                    labels.add("Failure");
                    labels.add("Success");
                } else if (experimentType.equals(Extras.MEASUREMENT_TYPE) && diffForBin < 1) {
                    // Handle the special case for the range of the bin here
                    double[] mins = new double[requiredBin];
                    double[] maxs = new double[requiredBin];

                    mins[0] = experimentData.get(0);
                    for (int i = 1; i < requiredBin; i++) {
                        mins[i] = mins[i-1] + diffForBins;
                    }

                    maxs[0] = experimentData.get(0) + diffForBins;
                    for (int j = 1; j < requiredBin; j++) {
                        maxs[j] = maxs[j-1] + diffForBins;
                    }

                    for (int i = 0; i < requiredBin; i++) {
                        // For the last bin, it includes all the numbers above the value
                        // For the regular bin, its range is from min to max.
                        if (i == requiredBin - 1) {
                            String min = (df.format(mins[i]));
                            String label = min + "+";
                            labels.add(label);
                        } else {
                            String min = df.format(mins[i]);
                            String max = df.format(maxs[i]);
                            String label = min + '-' + max;
                            labels.add(label);
                        }
                    }
                } else {
                    for (int i = 0; i < requiredBin; i++) {
                        // For the last bin, it includes all the numbers above the value
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
            // When the required Bin is smaller than 5, get the exact element as the name for the bin
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

    /**
     * Helper Method: Obtain the distinct value in the experiment
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
     * Helper Method
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
     * Helper Method: Calculate the maximum value for each bin
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
     * Helper Method: Calculate the minimum value for each bin
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

        // For the MeasurementType Trial, the bins will be classified as
        // E.g. 1-2 (first bin), 2-3 (second bin)
        // The maximum value of the bin is exclusive.
        if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
            for (int i = 1; i < requiredBin; i++) {
                minForEachBin[i] = maxForEachBin[i - 1];
            }
        }
        // For the Non Negative Trial, the bins will be classified as
        // E.g. 1-2 (first bin), 3-4 (second bin)
        // The maximum value of the bin is inclusive since the type for the count is int.
        else {
            minForEachBin[0] = 0;
            for (int i = 1; i < requiredBin; i++) {
                minForEachBin[i] = maxForEachBin[i - 1] + 1;
            }
        }

        // For Count Trial, the labels (Count) will be added directly in getLabels Method.
        // For Binomial Trial, the labels (Failure, Success) will be added directly in
        // getLabels Method.

        return minForEachBin;
    }

    /**
     * Helper Method: Collect the frequency for each bins (At least equal or more than 5 bins)
     * @param noOfBin
     * The number of bins we are going to created for the Histogram
     * @return
     * Return the arrayList of integer contains the frequency for each bin
     * in ascending order.
     */
    public ArrayList<Integer> getBinFrequencyA(int noOfBin) {
        ArrayList<Integer> binFrequency = new ArrayList<>();

        double range = rangeOfData();

        int frequency;

        Double diffForBins = range / noOfBin;
        int diffForBin = diffForBins.intValue();

        if (experimentType.equals(Extras.MEASUREMENT_TYPE) && diffForBin == 0) {
            double[] mins = new double[noOfBin];
            double[] maxs = new double[noOfBin];

            mins[0] = experimentData.get(0);
            for (int i = 1; i < noOfBin; i++) {
                mins[i] = mins[i-1] + diffForBins;
            }

            maxs[0] = experimentData.get(0) + diffForBins;
            for (int j = 1; j < noOfBin; j++) {
                maxs[j] = maxs[j-1] + diffForBins;
            }

            for (int i = 0; i < noOfBin; i++) {
                frequency = 0;
                for (int j = 0; j < experimentData.size(); j++) {
                    // The last bin, only check if the value is larger than the minForEachBin[i] value
                    // Regular bins, check if the value is larger than the minForEachBin[i] value and
                    // smaller than maxForEachBin[i] value
                    if (i == noOfBin - 1) {
                        if (experimentData.get(j) >= mins[i]) {
                            frequency += 1;
                        }
                    } else {
                        if (experimentData.get(j) >= mins[i] && experimentData.get(j) < maxs[i]) {
                            frequency += 1;
                        }
                    }
                }
                binFrequency.add(frequency);
            }
        } else{
                int[] maxForEachBin = getMaxForEachBin(diffForBin, noOfBin);
                int[] minForEachBin = getMinForEachBin(maxForEachBin, noOfBin);

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
                            if (experimentData.get(j) >= minForEachBin[i] && experimentData.get(j) < maxForEachBin[i]) {
                                frequency += 1;
                            }
                        }
                    }
                    binFrequency.add(frequency);
                }
            }

        return binFrequency;
    }

    /**
     * Helper Method: Obtain the category (label) for each bin which are used to plot the histogram
     * @param requiredBin
     * The number of bins we are going to created for the Histogram
     * @return
     * An list of double value represents as the label for the x-axis on the histogram
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
     * Helper Method: Collect the frequency for each bins (Less than 5 bins)
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