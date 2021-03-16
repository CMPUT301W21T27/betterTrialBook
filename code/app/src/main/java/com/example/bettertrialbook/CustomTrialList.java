/*
Custom list for displaying trials in an experiment view
*/

package com.example.bettertrialbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;

import java.util.ArrayList;

public class CustomTrialList extends ArrayAdapter<Trial> {

    private final Context context;
    private final ArrayList<Trial> trials;

    public CustomTrialList(Context context, ArrayList<Trial> trials) {
        super(context, 0, trials);
        this.trials = trials;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_trial, parent, false);
        }

        TextView trialNumber = view.findViewById(R.id.trialNumber_textView);
        TextView trialResult = view.findViewById(R.id.trialResult_textView);

        trialNumber.setText(String.valueOf(position + 1));

        Trial trial = trials.get(position);
        String trialType = trial.getTrialType();

        Log.d("tag", "Trial type is: " + trialType);

        // Need to set the result to a different format for each kind of trial type
        if (trialType.equals(Extras.COUNT_TYPE)) {
            CountTrial countTrial = (CountTrial) trial;
            trialResult.setText(String.valueOf(countTrial.getCount()));
        } else if (trialType.equals(Extras.BINOMIAL_TYPE)) {
            BinomialTrial binomialTrial = (BinomialTrial) trial;
            trialResult.setText(binomialTrial.getPassCount() + " " + binomialTrial.getFailCount());
        } else if (trialType.equals(Extras.NONNEG_TYPE)) {
            NonNegTrial nonNegTrial = (NonNegTrial) trial;
            trialResult.setText(String.valueOf(nonNegTrial.getCount()));
        } else if (trialType.equals(Extras.MEASUREMENT_TYPE)) {
            MeasurementTrial measurementTrial = (MeasurementTrial) trial;
            trialResult.setText(String.valueOf(measurementTrial.getMeasurement()));
        }

        return view;
    }
}