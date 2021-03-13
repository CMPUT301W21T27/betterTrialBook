package com.example.bettertrialbook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;

import java.util.UUID;

/*
 * https://developer.android.com/reference/android/app/DialogFragment
 */
public class AddTrialDialogFragment extends DialogFragment {
    // when creating an instance of the fragment, we will store the type of trial/experiment as well as the
    // id of the selected experiment. These can be accessed throughout the fragment
    static AddTrialDialogFragment newInstance(String trialType, String experimentId) {
        AddTrialDialogFragment f = new AddTrialDialogFragment();

        Bundle args = new Bundle();
        args.putString("trialType", trialType);
        args.putString("experimentId", experimentId);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle trialBundle = this.getArguments();
        String trialType = (String) trialBundle.get("trialType");
        String experimentId = (String) trialBundle.get("experimentId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // access to the array of experiment types (can only be done with a view context so
        // hardcoded in some other classes)
        // First is 'Count-Based'
        if (trialType.equals(Extras.COUNT_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_countbased_trial, null);
            EditText countEditText = view.findViewById(R.id.count_editText);
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CountTrial trial = new CountTrial(Integer.parseInt(String.valueOf(countEditText.getText())), UUID.randomUUID().toString());
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            // use the dal to add the trial to the db
                            experimentDAL.addTrial(experimentId, trial);
                        }
                    }).create();
        // 'Binomial'
        } else if (trialType.equals(Extras.BINOMIAL_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_binomial_trial, null);
            // use these weird final one element arrays for access in the onClick function
            final int[] successes = {0};
            final int[] failures = {0};
            TextView successTextView = view.findViewById(R.id.success_textView);
            TextView failureTextView = view.findViewById(R.id.failure_textView);
            Button successIncrementButton = view.findViewById(R.id.successIncrement_button);
            successIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    successes[0] += 1;
                    successTextView.setText("Successes: " + successes[0]);
                }
            });
            Button successDecrementButton = view.findViewById(R.id.successDecrement_button);
            successDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (successes[0] > 0) {
                        successes[0] -= 1;
                        successTextView.setText("Successes: " + successes[0]);
                    }
                }
            });
            Button failureIncrementButton = view.findViewById(R.id.failureIncrement_button);
            failureIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    failures[0] += 1;
                    failureTextView.setText("Failures: " + failures[0]);
                }
            });
            Button failureDecrementButton = view.findViewById(R.id.failureDecrement_button);
            failureDecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (failures[0] > 0) {
                        failures[0] -= 1;
                        failureTextView.setText("Failures: " + failures[0]);
                    }
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BinomialTrial trial = new BinomialTrial(successes[0], failures[0], UUID.randomUUID().toString());
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            experimentDAL.addTrial(experimentId, trial);
                        }
                    }).create();
        // 'Non-Negative Integer'
        } else if (trialType.equals(Extras.NONNEG_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_nonnegative_trial, null);
            EditText amountEditText = view.findViewById(R.id.intamount_editText);
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NonNegTrial trial = new NonNegTrial(Integer.parseInt(String.valueOf(amountEditText.getText())), UUID.randomUUID().toString());
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            experimentDAL.addTrial(experimentId, trial);
                        }
                    }).create();
        // 'Measurement'
        } else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_measurement_trial, null);
            EditText amountEditText = view.findViewById(R.id.amount_editText);
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MeasurementTrial trial = new MeasurementTrial(Double.parseDouble(String.valueOf(amountEditText.getText())), UUID.randomUUID().toString());
                            ExperimentDAL experimentDAL = new ExperimentDAL();
                            experimentDAL.addTrial(experimentId, trial);
                        }
                    }).create();
        }
    }
}
