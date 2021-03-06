package com.example.bettertrialbook.statistic;

import android.util.Log;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.Statistic;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.MeasurementTrial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;

/**
 * A model to to hold relevant data for the statistics and line graph
 */
public class LineGraphInfo {
    DecimalFormat df;
    Statistic statistic;
    SimpleDateFormat sdf;
    private String experimentType;
    private ArrayList<Trial> trials;

    /**
     * An constructor initialize the methods related to the LineChart
     * @param trials
     * The trial data for the experiment
     * @param type
     * The type of the experiment
     */
    public LineGraphInfo(ArrayList<Trial> trials, String type) {
        this.trials = trials;
        this.experimentType = type;
        this.statistic = new Statistic();
        this.df = new DecimalFormat("#.####");
        this.sdf = new SimpleDateFormat("yyyy.MM.dd");
    }

    /**
     * Obtains the mean Over time (number of trials as time progresses)
     * @return
     * An arrayList of double value contains the current mean respect to the number
     * of trials at that current time. (in ascending order)
     */
    public ArrayList<Double> MeanOverTime() {
        double sum;             // Contains the summation value
        double mean;            // Hold the current mean
        double size;            // Contains the current size (as the number of trial progress)
        double value = 0;
        ArrayList<Double> meanOverTime = new ArrayList<>();

        // Remarks: CountTrial does not use any MeanOverTime method
        // As the mean over time is meaningless data to count trial. (Disabled in UI)
        if (trials != null && trials.size() > 0) {
            for (int i = 0; i < trials.size(); i++) {
                sum = 0;
                size = 0;
                // The for loop calculates the mean over time (over the number of trials
                // at that current time)
                for (int j = 0; j <= i; j++) {
                    if (experimentType.equals(Extras.NONNEG_TYPE)) {
                        NonNegTrial nonNegTrial = (NonNegTrial) trials.get(j);
                        value = nonNegTrial.getCount();
                    }
                    if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                        MeasurementTrial measurementTrial = (MeasurementTrial) trials.get(j);
                        value = measurementTrial.getMeasurement();
                    }
                    if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                        BinomialTrial binomialTrial = (BinomialTrial) trials.get(j);
                        if (binomialTrial.getSuccess()) {
                            value = 1;
                        } else {
                            value = 0;
                        }
                    }
                    size += 1;
                    sum += value;
                }
                mean = Double.parseDouble(df.format(sum / size));
                meanOverTime.add(mean);
            }
        } else {
            meanOverTime = null;
        }

        return meanOverTime;
    }

    /**
     * Obtains the standard deviations over time (number of trials as time progresses)
     * @param meanOverTime
     * An arrayList of double value contains the mean at that current time.
     * @return
     * An arrayList of double value contains the current standard deviation
     * respect to the number of trials at that current time. (in asecending order)
     */
    public ArrayList<Double> StdDevOverTime(ArrayList<Double> meanOverTime) {
        double sum;             // Contains the summation value
        double size;            // Contains the current size (as the number of trial progress)
        double stdDev;          // Hold the current standard deviation
        double value = 0;       // Used to store the immediate value
        ArrayList<Double> stdDevOverTime = new ArrayList<>();

        if (trials.size() == 0) {
            return null;
        } else {
            // Remarks: CountTrial does not use stdDevOverTime method as
            // the standard Deviation over time is meaningless data to count trial. (Disabled in UI)
            for (int i = 0; i < trials.size(); i++) {
                sum = 0;
                size = 0;
                for (int j = 0; j <= i; j++) {
                    if (experimentType.equals(Extras.NONNEG_TYPE)) {
                        NonNegTrial nonNegTrial = (NonNegTrial) trials.get(j);
                        value = Math.pow((nonNegTrial.getCount() - meanOverTime.get(i)), 2);
                    }
                    if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                        MeasurementTrial measurementTrial = (MeasurementTrial) trials.get(j);
                        value = Math.pow((measurementTrial.getMeasurement() - meanOverTime.get(i)), 2);
                    }
                    if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                        BinomialTrial binomialTrial = (BinomialTrial) trials.get(j);
                        if (binomialTrial.getSuccess()) {
                            value = Math.pow((1.0 - meanOverTime.get(i)), 2);
                        } else {
                            value = Math.pow((0.0 - meanOverTime.get(i)), 2);
                        }
                    }
                    size += 1;
                    sum += value;
                }
                stdDev = Double.parseDouble(df.format(Math.sqrt(sum / size)));
                stdDevOverTime.add(stdDev);
            }
        }

        return stdDevOverTime;
    }

    /**
     * Obtains the median over time (number of trials as time progresses)
     * @return
     * An arrayList of double value contains the current median
     * respect to the number of trials at that current time. (in ascending order)
     */
    public ArrayList<Double> MedianOverTime() {
        double median;
        ArrayList<Double> dataList;                           // Hold the data value up to the current trial
        ArrayList<Trial> temporaryArray = new ArrayList<>();  // Hold the trials up to the current number
        ArrayList<Double> medianOverTime = new ArrayList<>();

        // Remarks: CountTrial does not use MedianOverTime method
        // As the median over time is meaningless data to count trial. (Disabled in UI)
        if (trials.size() > 0) {
            for (int i = 0; i < trials.size(); i++) {
                temporaryArray.clear();
                for (int j = 0; j <= i; j++) {
                    temporaryArray.add(trials.get(j));
                }
                // dataList is a sorted in non-descending order arrayList
                dataList = statistic.experimentData(temporaryArray);
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

    public ArrayList<Double> ResultOverTime() {
        double frequency = 0;
        ArrayList<Double> result = new ArrayList<>();
        ArrayList<String> dates = getTheDates();

        if (trials != null && trials.size() > 0) {
            for (String date : dates) {
                frequency = 0;
                for (Trial trial: trials) {
                    String theDate = sdf.format(trial.getTimestamp());
                    if (experimentType.equals(Extras.COUNT_TYPE)) {
                        if (theDate.equals(date)) {
                            frequency += 1;
                        }
                    }
                    if (experimentType.equals(Extras.NONNEG_TYPE)) {
                        NonNegTrial nonNegTrial = (NonNegTrial) trial;
                        if (theDate.equals(date)) {
                            frequency += (double) nonNegTrial.getCount();
                        }
                    }
                    if (experimentType.equals(Extras.MEASUREMENT_TYPE)) {
                        MeasurementTrial measurementTrial = (MeasurementTrial) trial;
                        if (theDate.equals(date)) {
                            frequency += measurementTrial.getMeasurement();
                        }
                    }
                    if (experimentType.equals(Extras.BINOMIAL_TYPE)) {
                        BinomialTrial binomialTrial = (BinomialTrial) trial;
                        // Either success or fail will count towards the frequency
                        // for the number of trials per day
                        if (theDate.equals(date)) {
                            frequency += 1;
                        }
                    }
                }
                result.add(frequency);
            }
        } else {
            result = null;
        }

        return result;
    }


    /**
     * Helper Method: Obtain the distinct dates (yyyy.MM.dd) for the trails in the experiment
     * @return
     * An arrayList of String of dates (MMM DD YYYY)
     */
    public ArrayList<String> getTheDates() {
        ArrayList<String> date = new ArrayList<>();
        Set<String> dateString = new LinkedHashSet<>();

        // Add the String format (yyyy.MM.dd) into the arrayList
        if (trials != null && trials.size() > 0) {
            for (Trial trial : trials) {
                date.add(sdf.format(trial.getTimestamp()));
            }
        }

        // Find the distinct date in the arrayList
        dateString.addAll(date);
        date.clear();
        date.addAll(dateString);

        return date;
    }
}
