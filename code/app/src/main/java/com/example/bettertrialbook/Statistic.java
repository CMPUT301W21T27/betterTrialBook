package com.example.bettertrialbook;

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
     * Calculate the overall mean of the experiment depends on the experiment Type
     * @param trials
     * The trial data for the experiment
     * @param experimentType
     * The type of the experiment (Count / Measurement / Non-Negative / Binomial)
     * @return
     * The mean of the experiment
     */
    public double Mean(ArrayList<Trial> trials, String experimentType) {
        double mean;
        double sum = 0;
        double value = 0;

        for (Trial trial: trials) {
            if (experimentType.equals(Extras.COUNT_TYPE)) {
                CountTrial countTrial = (CountTrial) trial;
                value = countTrial.getCount();
            }
            if (experimentType.equals(Extras.NONNEG_TYPE)) {
                NonNegTrial nonNegTrial = (NonNegTrial) trial;
                value = nonNegTrial.getCount();
            }
            if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                value = measurementTrial.getMeasurement();
            }
            if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                // "TO-DO" Add the mean calculation method to binomial;
            }
            sum += value;
        }

        if (trials.size() != 0) {
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
     * @param experimentType
     * The type of the experiment (Count / Measurement / Non-Negative / Binomial)
     * @param mean
     * The mean of the experiment, which can be obtained using method Mean
     * @return
     * The standard deviation of the experiment
     */
    public double StdDev(ArrayList<Trial> trials, String experimentType, double mean) {
        double stdDev;
        double sum = 0;
        double value = 0;               // Used to store the immediate value

        for (Trial trial: trials) {
            if (experimentType.equals(Extras.COUNT_TYPE)) {
                CountTrial countTrial = (CountTrial) trial;
                value = Math.pow((countTrial.getCount() - mean), 2);
            }
            if (experimentType.equals(Extras.NONNEG_TYPE)) {
                NonNegTrial nonNegTrial = (NonNegTrial) trial;
                value = Math.pow((nonNegTrial.getCount() - mean), 2);
            }
            if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                value = Math.pow((measurementTrial.getMeasurement() - mean), 2);
            }
            if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                BinomialTrial binomialTrial = (BinomialTrial) trial;
                // "TO-DO" Add the mean calculation method to binomial;
            }
            sum += value;
        }

        if (trials.size() != 0) {
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
     * @param experimentType
     * The type of the experiment (Count / Measurement / Non-Negative / Binomial)
     * @return
     * The median of the experiment's data
     */
    public double Median(ArrayList<Trial> trials, String experimentType) {
        double median;
        ArrayList<Double> dataList = new ArrayList<>();     // Temporary ArrayList used to Sort the data

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
                // "TO-DO" Add the mean calculation method to binomial;
            }
        }

        Collections.sort(dataList);

        if (trials.size() != 0) {
            // For Odd Count Data Set
            if (dataList.size() % 2 == 1) {
                median = Double.parseDouble(df.format(dataList.get(dataList.size() / 2 - 1)));
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
     * @param experimentType
     * The type of the experiment (Count / Measurement / Non-Negative / Binomial)
     * @return
     * A List which contains the First Quartile Value and the Third Quartile Value
     */
    public double[] Quartiles(ArrayList<Trial> trials, String experimentType) {
        int medianIndex;
        double firstQuartile;
        double thirdQuartile;
        double[] quartiles = new double[2];
        ArrayList<Double> dataList = new ArrayList<>();     // Temporary ArrayList used to Sort the data

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
                // "TO-DO" Add the mean calculation method to binomial;
            }
        }

        Collections.sort(dataList);

        if (trials.size() != 0) {
            if (trials.size() % 2 == 1) {
                medianIndex = trials.size() / 2 - 1;        // For Odd Count Data Set
            } else {
                medianIndex = trials.size() / 2;            // For Even Count Data Set
            }
            firstQuartile = firstQuartile(dataList, medianIndex);
            thirdQuartile = thirdQuartile(dataList, medianIndex);
            quartiles[0] = firstQuartile;
            quartiles[1] = thirdQuartile;
        }

        return quartiles;
    }

    //  ----------------------------Helper Methods Below-----------------------------------------
    public double firstQuartile (ArrayList<Double> dataList, int medianIndex) {
        double quartile = 0;
        ArrayList<Double> quartileDataSet = new ArrayList<>();

        for (int i = 0; i < medianIndex; i++) {
            quartileDataSet.add(dataList.get(i));
        }

        quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);

        return quartile;
    }

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

        quartile = ((quartileDataSet.get(quartileDataSet.size() / 2) + quartileDataSet.get(quartileDataSet.size() / 2 - 1)) / 2);

        return quartile;
    }
}
