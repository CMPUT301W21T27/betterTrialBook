/*
    Plot the mean, median, standard deviation change over time.
 */
package com.example.bettertrialbook.statistic;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class LineGraphInfo {
    Statistic statistic = new Statistic();
    DecimalFormat df = new DecimalFormat("#.###");

    public LineGraphInfo() { /* Empty Constructor */}

    public ArrayList<Double> MeanOverTime(ArrayList<Trial> trials) {
        double sum;
        double size;
        double mean;
        double value = 0;
        ArrayList<Double> meanOverTime = new ArrayList<>();

        if (trials.size() == 0) {
            return null;
        } else {
            for (int i = 0; i < trials.size(); i++) {
                sum = 0;
                size = 0;
                for (int j = 0; j <= i; j++) {
                    if (trials.get(j).getTrialType().equals(Extras.COUNT_TYPE)) {
                        CountTrial countTrial = (CountTrial) trials.get(j);
                        // value = countTrial.getCount();
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.NONNEG_TYPE)) {
                        NonNegTrial nonNegTrial = (NonNegTrial) trials.get(j);
                        value = nonNegTrial.getCount();
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                        MeasurementTrial measurementTrial = (MeasurementTrial) trials.get(j);
                        value = measurementTrial.getMeasurement();
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                        BinomialTrial binomialTrial = (BinomialTrial) trials.get(j);
                        /*
                        value = binomialTrial.getPassCount();
                        size += (binomialTrial.getFailCount() + binomialTrial.getPassCount());

                         */
                    }
                    sum += value;
                }
                mean = Double.parseDouble(df.format(sum / size));
                meanOverTime.add(mean);
            }
        }

        return meanOverTime;
    }

    public ArrayList<Double> StdDevOverTime(ArrayList<Trial> trials, ArrayList<Double> meanOverTime) {
        double stdDev;
        double sum = 0;
        double size = 0;
        double value = 0; // Used to store the immediate value
        ArrayList<Double> stdDevOverTime = new ArrayList<>();


        if (trials.size() == 0) {
            return null;
        } else {
            for (int i = 0; i < trials.size(); i++) {
                sum = 0;
                size = 0;
                for (int j = 0; j <= i; j++) {
                    if (trials.get(j).getTrialType().equals(Extras.COUNT_TYPE)) {
                        CountTrial countTrial = (CountTrial) trials.get(j);
                        // value = Math.pow((countTrial.getCount() - meanOverTime.get(i)), 2);
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.NONNEG_TYPE)) {
                        NonNegTrial nonNegTrial = (NonNegTrial) trials.get(j);
                        value = Math.pow((nonNegTrial.getCount() - meanOverTime.get(i)), 2);
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                        MeasurementTrial measurementTrial = (MeasurementTrial) trials.get(j);
                        value = Math.pow((measurementTrial.getMeasurement() - meanOverTime.get(i)), 2);
                        size += 1;
                    }
                    if (trials.get(j).getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                        BinomialTrial binomialTrial = (BinomialTrial) trials.get(j);
                        /*
                        value = binomialTrial.getPassCount() * Math.pow((1 - meanOverTime.get(i)), 2);
                        value += binomialTrial.getFailCount() * Math.pow((0 - meanOverTime.get(i)), 2);
                        size += (binomialTrial.getFailCount() + binomialTrial.getPassCount());

                         */
                    }
                    sum += value;
                }
                stdDev = Double.parseDouble(df.format(Math.sqrt(sum / size)));
                stdDevOverTime.add(stdDev);
            }
        }

        return stdDevOverTime;
    }

    public ArrayList<Double> MedianOverTime(ArrayList<Trial> trials) {
        double median = 0;
        String experimentType;
        ArrayList<Double> dataList;
        ArrayList<Double> medianOverTime = new ArrayList<>();
        ArrayList<Trial> temporaryArray = new ArrayList<>();   // Used as temporary Array

        if (trials.size() > 0) {
            experimentType = trials.get(0).getTrialType();
            for (int i = 0; i < trials.size(); i++) {
                temporaryArray.clear();
                for (int j = 0; j <= i; j++) {
                    temporaryArray.add(trials.get(j));
                }
                dataList = statistic.SortedArrayList(temporaryArray, experimentType);
                // For Odd Count Data Set
                if (dataList.size() % 2 == 1) {
                    median = Double.parseDouble(df.format(dataList.get(dataList.size() / 2)));
                }
                // For Even Count Data Set
                else {
                    median = ( dataList.get(dataList.size() / 2) + (dataList.get(dataList.size() / 2 - 1)) ) / 2;
                    median = Double.parseDouble(df.format(median));
                }
                medianOverTime.add(median);
            }
        } else {
            return null;
        }

        return medianOverTime;
    }
}
