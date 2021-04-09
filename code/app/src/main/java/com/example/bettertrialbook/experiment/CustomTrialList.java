package com.example.bettertrialbook.experiment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.models.User;

import java.util.ArrayList;

/**
 * A CustomList for displaying trials in an experiment view.
 */
public class CustomTrialList extends ArrayAdapter<Trial> {

    private boolean isOwner;
    private final Context context;
    private final ArrayList<Trial> trials;
    private String experimentId;

    public CustomTrialList(Context context, ArrayList<Trial> trials, String experimentId, Boolean isOwner) {
        super(context, 0, trials);
        this.trials = trials;
        this.context = context;
        this.isOwner = isOwner;
        this.experimentId = experimentId;
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
        TextView experimenterIdText = view.findViewById(R.id.trialExperimenter_textView);

        trialNumber.setText(String.valueOf(position + 1));

        Trial trial = trials.get(position);
        String trialType = trial.getTrialType();
        String experimenterId = trial.getExperimenterID();

        Log.d("tag", "Trial type is: " + trialType);
        if (experimenterId != null) {
            // get experimenter username
            UserDAL userDAL = new UserDAL();
            userDAL.findUserByID(experimenterId, new UserDAL.FindUserByIDCallback() {
                @Override
                public void onCallback(User user) {
                    if (user != null) {
                        if (!user.getUsername().equals("")) {
                            experimenterIdText.setText("Experimenter: " + user.getUsername());
                        } else {
                            experimenterIdText.setText("Experimenter: " + user.getID().substring(0, 8));
                        }
                    } else {
                        if (experimenterId.length() >= 8) {
                            experimenterIdText.setText("Experimenter: " + experimenterId.substring(0, 8));
                        } else {
                            experimenterIdText.setText("Experimenter: " + experimenterId);
                        }

                    }
                }
            });
        }

        // Need to set the result to a different format for each kind of trial type
        if (trialType.equals(Extras.COUNT_TYPE)) {
            CountTrial countTrial = (CountTrial) trial;
            trialResult.setText("Observed");

        } else if (trialType.equals(Extras.BINOMIAL_TYPE)) {
            BinomialTrial binomialTrial = (BinomialTrial) trial;
            if (binomialTrial.getSuccess()) {
                trialResult.setText("Success");
            } else {
                trialResult.setText("Failure");
            }

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
