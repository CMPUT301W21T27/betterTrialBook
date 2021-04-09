package com.example.bettertrialbook.models;

import com.example.bettertrialbook.Extras;

import java.util.ArrayList;
import java.util.Collections;
import java.text.DecimalFormat;

public class Statistic {

    DecimalFormat df = new DecimalFormat("#.######");

    /**
     * Initialize the calculation methods related to Statistic
     * It is a Empty Constructor.
     */
    public Statistic() { }

    /**
     * Sorted the trials' data of the experiment
     * @param trials
     * The trial data for the experiment
     * @return
     * Return a sorted arrayList of data from each trial
     */
    public ArrayList<Double> experimentData(ArrayList<Trial> trials) {
        ArrayList<Double> data = new ArrayList<>();

        if (trials.size() > 0) {
            for (Trial trial : trials) {
                if (trial.getTrialType().equals(Extras.COUNT_TYPE)) {
                    data.add(1.0);
                }
                if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                    NonNegTrial nonNegTrial = (NonNegTrial) trial;
                    data.add((double) nonNegTrial.getCount());
                }
                if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                    BinomialTrial binomialTrial = (BinomialTrial) trial;
                    // Success : 1.0 ; Failure : 0.0
                    if (binomialTrial.getSuccess()) {
                        data.add(1.0);
                    } else {
                        data.add(0.0);
                    }
                }
                if (trial.getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                    MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                    data.add(measurementTrial.getMeasurement());
                }
            }

            Collections.sort(data);
        }

        return data;
    }

    /**
     * Calculate the overall mean of the experiment depends on the experiment Type
     * @param trials
     * The trial data for the experiment
     * @return
     * The mean of the experiment
     */
    public double Mean(ArrayList<Trial> trials) {
        double mean;
        double sum = 0;            // Represent the summation of the data
        double value = 0;          // Temporary holder for the data value

        if (trials != null && trials.size() != 0) {
            for (Trial trial : trials) {
                if (trial.getTrialType().equals(Extras.COUNT_TYPE)) {
                    value = 1;
                }
                if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                    NonNegTrial nonNegTrial = (NonNegTrial) trial;
                    value = nonNegTrial.getCount();
                }
                if (trial.getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                    MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                    value = measurementTrial.getMeasurement();
                }
                if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                    BinomialTrial binomialTrial = (BinomialTrial) trial;
                    if (binomialTrial.getSuccess()) {
                        value = 1;
                    } else {
                        value = 0;
                    }
                }
                // Adding the current trial's data into the summation
                sum += value;
            }
            mean = Double.parseDouble(df.format(sum / trials.size()));
        } else {
            mean = 0;
        }

        return mean;
    }

    /**
     * Calculate the standard deviation of the experiment depending on the experiment Type
     * @param trials
     * The trial data for the experiment
     * @param mean
     * The mean of the experiment, which can be obtained using method Mean
     * @return
     * The standard deviation of the experiment
     */
    public double StdDev(ArrayList<Trial> trials, double mean) {
        double stdDev;
        double sum = 0;         // Represents the summation of the data
        double value = 0;       // Used to store the immediate value

        if (trials != null && trials.size() != 0) {
            for (Trial trial : trials) {
                if (trial.getTrialType().equals(Extras.COUNT_TYPE)) {
                    value = Math.pow((1.0 - mean), 2);
                }
                if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                    NonNegTrial nonNegTrial = (NonNegTrial) trial;
                    value = Math.pow((nonNegTrial.getCount() - mean), 2);
                }
                if (trial.getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                    MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                    value = Math.pow((measurementTrial.getMeasurement() - mean), 2);
                }
                if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                    BinomialTrial binomialTrial = (BinomialTrial) trial;
                    if (binomialTrial.getSuccess()) {
                        value = Math.pow((1 - mean), 2);
                    } else {
                        value = Math.pow((0 - mean), 2);
                    }
                }
                // Adding the current value into the summation
                sum += value;
            }
            stdDev = Double.parseDouble(df.format(Math.sqrt(sum / trials.size())));
        } else {
            stdDev = 0;
        }

        return stdDev;
    }

    /**
     * Find the Median in the sorted Data set for the experiment
     * @param trials
     * The trial data for the experiment
     * @return
     * The median of the experiment's data
     */
    public double Median(ArrayList<Trial> trials) {
        double median;
        ArrayList<Double> dataList;     // Represents the sorted arrayList data

        if (trials != null && trials.size() > 0) {
            // Obtain the data of the experiment in sorted (non-decreasing) order
            dataList = experimentData(trials);
            // Obtain the median in Odd Count data set
            if (dataList.size() % 2 == 1) {
                median = Double.parseDouble(df.format(dataList.get(dataList.size() / 2)));
            }
            // Obtain the median in Even Count data set
            else {
                median = (dataList.get(dataList.size() / 2) + (dataList.get(dataList.size() / 2 - 1))) / 2;
                median = Double.parseDouble(df.format(median));
            }
        } else {
            median = 0;
        }

        return median;
    }

    /**
     * Find the 1st Quartile and the 3rd Quartile of the experiment
     * @param trials
     * The trial data for the experiment
     * @return
     * A List which contains the First Quartile Value and the Third Quartile Value
     * quartile[0] = first quartile value ; quartile[1] = third quartile value
     */
    public double[] Quartiles(ArrayList<Trial> trials ) {
        int medianIndex;
        double firstQuartile;
        double thirdQuartile;
        double[] quartiles = new double[2];     // Contains the firstQuartile and thirdQuartile value
        ArrayList<Double> dataList;             // Sorted ArrayList

        if (trials != null && trials.size() >= 1) {
            if (trials.size() > 1) {
                dataList = experimentData(trials);
                // For Odd DataSet , Median Index is the exact Medina Index
                // For Even DataSet, Median Index is the upper index
                medianIndex = dataList.size() / 2;
                firstQuartile = firstQuartile(dataList, medianIndex);
                thirdQuartile = thirdQuartile(dataList, medianIndex);
                quartiles[0] = Double.parseDouble(df.format(firstQuartile));
                quartiles[1] = Double.parseDouble(df.format(thirdQuartile));
            }
            // Although Quartile information will become meaningless for single item
            // Use the first data value to represent both first Quartile and third Quartile
            else if (trials.size() == 1) {
                if (trials.get(0).getTrialType().equals(Extras.COUNT_TYPE)) {
                    CountTrial countTrial = (CountTrial) trials.get(0);
                    quartiles[0] = 1.0;
                    quartiles[1] = 1.0;
                }
                if (trials.get(0).getTrialType().equals(Extras.NONNEG_TYPE)) {
                    NonNegTrial nonNegTrial = (NonNegTrial) trials.get(0);
                    quartiles[0] = Double.parseDouble(df.format(nonNegTrial.getCount()));
                    quartiles[1] = Double.parseDouble(df.format(nonNegTrial.getCount()));
                }
                if (trials.get(0).getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                    MeasurementTrial measurementTrial = (MeasurementTrial) trials.get(0);
                    quartiles[0] = Double.parseDouble(df.format(measurementTrial.getMeasurement()));
                    quartiles[1] = Double.parseDouble(df.format(measurementTrial.getMeasurement()));
                }
                if (trials.get(0).getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                    BinomialTrial binomialTrial = (BinomialTrial) trials.get(0);
                    if (binomialTrial.getSuccess()) {
                        quartiles[0] = 1.0;
                        quartiles[1] = 1.0;
                    } else {
                        quartiles[0] = 0.0;
                        quartiles[1] = 0.0;
                    }
                }
            }
        } else {
            quartiles[0] = 0.0;
            quartiles[1] = 0.0;
        }

        return quartiles;
    }

    /**
     * Helper Methods: Calculate the first Quartile in the dataSet
     * @param dataList
     * The data for each trial in the experiment
     * @param medianIndex
     * The index in the dataList that is the median of the dataList
     * @return
     * The value of the first Quartile
     */
    public double firstQuartile (ArrayList<Double> dataList, int medianIndex) {
        double quartile = 0;
        ArrayList<Double> quartileDataSet = new ArrayList<>();

        for (int i = 0; i < medianIndex; i++) {
            quartileDataSet.add(dataList.get(i));
        }

        // For Even count data set obtained above
        if (quartileDataSet.size() % 2 == 0) {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);
        }
        // For Odd count data set obtained above
        else {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2)));
        }

        return quartile;
    }

    /**
     * Helper Methods: Calculate the third Quartile in the dataSet
     * @param dataList
     * The data for each trial in the experiment
     * @param medianIndex
     * The index in the dataList that is the median of the dataList
     * @return
     * The value of the third Quartile
     */
    public double thirdQuartile (ArrayList<Double> dataList, int medianIndex) {
        double quartile = 0;
        ArrayList<Double> quartileDataSet = new ArrayList<>();

        // For Odd Count data set
        if (dataList.size() % 2 == 1) {
            for (int i = medianIndex + 1; i < dataList.size(); i++) {
                quartileDataSet.add(dataList.get(i));
            }
        }
        // For Even Count data set
        else {
            for (int i = medianIndex; i < dataList.size(); i++) {
                quartileDataSet.add(dataList.get(i));
            }
        }

        // For Even Count sub data set obtained above
        if (quartileDataSet.size() % 2 == 0) {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);
        }
        // For Odd Count sub data set obtained above
        else {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2)));
        }

        return quartile;
    }
}
