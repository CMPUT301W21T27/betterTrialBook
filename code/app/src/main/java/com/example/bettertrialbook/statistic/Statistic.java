package com.example.bettertrialbook.statistic;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.MeasurementTrial;

import java.util.ArrayList;
import java.util.Collections;
import java.text.DecimalFormat;

public class Statistic {

    DecimalFormat df = new DecimalFormat("#.###");

    /**
     * Initialize the calculation methods related to Statistic
     * It is a Empty Constructor.
     */
    public Statistic() { }

    /**
     *
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
                    CountTrial countTrial = (CountTrial) trial;
                    data.add((double) countTrial.getCount());
                }
                if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                    NonNegTrial nonNegTrial = (NonNegTrial) trial;
                    data.add((double) nonNegTrial.getCount());
                }
                if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                    BinomialTrial binomialTrial = (BinomialTrial) trial;
                    for (int i = 0; i < binomialTrial.getPassCount(); i++) {
                        data.add(1.0);
                    }
                    for (int j = 0; j < binomialTrial.getFailCount(); j++) {
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
        double sum = 0;
        double size = 0;
        double value = 0;

        for (Trial trial: trials) {
            if (trial.getTrialType().equals(Extras.COUNT_TYPE)) {
                CountTrial countTrial = (CountTrial) trial;
                value = countTrial.getCount();
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                NonNegTrial nonNegTrial = (NonNegTrial) trial;
                value = nonNegTrial.getCount();
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                value = measurementTrial.getMeasurement();
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                value = binomialTrial.getPassCount();
                size += (binomialTrial.getPassCount() + binomialTrial.getFailCount());
            }
            sum += value;
        }

        if (trials.size() != 0) {
            mean = Double.parseDouble(df.format(sum / size));
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
        double sum = 0;
        double size = 0;
        double value = 0;               // Used to store the immediate value

        for (Trial trial: trials) {
            if (trial.getTrialType().equals(Extras.COUNT_TYPE)) {
                CountTrial countTrial = (CountTrial) trial;
                value = Math.pow((countTrial.getCount() - mean), 2);
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.NONNEG_TYPE)) {
                NonNegTrial nonNegTrial = (NonNegTrial) trial;
                value = Math.pow((nonNegTrial.getCount() - mean), 2);
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.MEASUREMENT_TYPE)) {
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                value = Math.pow((measurementTrial.getMeasurement() - mean), 2);
                size += 1;
            }
            if (trial.getTrialType().equals(Extras.BINOMIAL_TYPE)) {
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                value = binomialTrial.getPassCount() * Math.pow((1 - mean), 2);
                value += binomialTrial.getFailCount() * Math.pow((0 - mean), 2);
                size += binomialTrial.getPassCount() + binomialTrial.getFailCount();
            }
            sum += value;
        }
        
        if (trials.size() != 0) {
            stdDev = Double.parseDouble(df.format(Math.sqrt(sum / size)));
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
        ArrayList<Double> dataList;             // Temporary ArrayList used to Sort the data

        if (trials.size() > 0) {
            String experimentType = trials.get(0).getTrialType();
            dataList = SortedArrayList(trials, experimentType);
            // For Odd Count Data Set
            if (dataList.size() % 2 == 1) {
                median = Double.parseDouble(df.format(dataList.get(dataList.size() / 2)));
            }
            // For Even Count Data Set
            else {
                median = ( dataList.get(dataList.size() / 2) + (dataList.get(dataList.size() / 2 - 1)) ) / 2;
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
        double[] quartiles = new double[2];
        ArrayList<Double> dataList;             // Temporary ArrayList used to Sort the data

        if (trials.size() > 0) {
            String experimentType = trials.get(0).getTrialType();
            dataList = SortedArrayList(trials, experimentType);
            // For Odd DataSet , Median Index is the exact Medina Index
            // For Even DataSet, Median Index is the upper index
            medianIndex = dataList.size() / 2 ;
            firstQuartile = firstQuartile(dataList, medianIndex);
            thirdQuartile = thirdQuartile(dataList, medianIndex);
            quartiles[0] = Double.parseDouble(df.format(firstQuartile));
            quartiles[1] = Double.parseDouble(df.format(thirdQuartile));
        } else {
            quartiles[0] = 0.0;
            quartiles[1] = 0.0;
        }

        return quartiles;
    }

    //  ----------------------------Helper Methods Below-----------------------------------------
    // Calculate the value of the 1st Quartile
    public double firstQuartile (ArrayList<Double> dataList, int medianIndex) {
        double quartile = 0;
        ArrayList<Double> quartileDataSet = new ArrayList<>();

        for (int i = 0; i < medianIndex; i++) {
            quartileDataSet.add(dataList.get(i));
        }

        if (quartileDataSet.size() % 2 == 0) {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);
        }
        else {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2)));
        }

        return quartile;
    }

    // Calculate the value of the 3rd Quartile
    public double thirdQuartile (ArrayList<Double> dataList, int medianIndex) {
        double quartile = 0;
        ArrayList<Double> quartileDataSet = new ArrayList<>();

        if (dataList.size() % 2 == 1) {
            for (int i = medianIndex + 1; i < dataList.size(); i++) {
                quartileDataSet.add(dataList.get(i));
            }
        }
        else {
            for (int i = medianIndex; i < dataList.size(); i++) {
                quartileDataSet.add(dataList.get(i));
            }
        }

        if (quartileDataSet.size() % 2 == 0) {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);
        }
        else {
            quartile = ((quartileDataSet.get(quartileDataSet.size() / 2)));
        }

        return quartile;
    }

    // Sorted the data of the experiment in ascending order
    public ArrayList<Double> SortedArrayList (ArrayList<Trial> trials, String experimentType) {
        ArrayList<Double> dataList = new ArrayList<>();
        for (Trial trial: trials) {
            if (experimentType.equals(Extras.COUNT_TYPE)) {
                CountTrial countTrial = (CountTrial) trial;
                dataList.add((double) countTrial.getCount());
            }
            if (experimentType.equals(Extras.NONNEG_TYPE)) {
                NonNegTrial nonNegTrial = (NonNegTrial) trial;
                dataList.add((double) nonNegTrial.getCount());
            }
            if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                dataList.add(measurementTrial.getMeasurement());
            }
            if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                // Add the Successful trials
                for (int i = 0; i < binomialTrial.getFailCount(); i++) {
                    dataList.add(0.0);
                }
                // Add the Fail trials
                for (int j = 0; j < binomialTrial.getPassCount(); j++) {
                    dataList.add(1.0);
                }
            }
        }

        Collections.sort(dataList);

        return dataList;
    }
}
