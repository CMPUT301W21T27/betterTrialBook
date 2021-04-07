/*
    Fragment for adding a new trial to an experiment. Uses the ExperimentDAL to
    add the new trial to the database.
*/
package com.example.bettertrialbook.experiment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.BinomialTrial;
import com.example.bettertrialbook.models.CountTrial;
import com.example.bettertrialbook.models.Geolocation;
import com.example.bettertrialbook.models.MeasurementTrial;
import com.example.bettertrialbook.models.NonNegTrial;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/*
 * https://developer.android.com/reference/android/app/DialogFragment
 */
public class AddTrialDialogFragment extends DialogFragment {
    Geolocation geolocation = new Geolocation(null);
    // when creating an instance of the fragment, we will store the type of trial/experiment as well as the
    // id of the selected experiment. These can be accessed throughout the fragment
    static AddTrialDialogFragment newInstance(String trialType, String experimentId, boolean geolocationRequired) {
        AddTrialDialogFragment f = new AddTrialDialogFragment();

        Bundle args = new Bundle();
        args.putString("trialType", trialType);
        args.putString("experimentId", experimentId);
        args.putBoolean("geolocationRequired", geolocationRequired);
        f.setArguments(args);

        return f;
    }

    // the onActivityResult method was only recently deprecated (~6 months ago)
    // thus with the new implementation slightly more complex and not necessarily stable,
    // we have decided to keep using this method for the current project
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("trial", "got result");
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // receive the selected geolocation from the map activity
            geolocation = data.getParcelableExtra("geolocation");
            Log.d("trial", String.valueOf(geolocation.getLocation().getLatitude()));
            Log.d("trial", String.valueOf(geolocation.getLocation().getLongitude()));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle trialBundle = this.getArguments();
        String trialType = (String) trialBundle.get("trialType");
        String experimentId = (String) trialBundle.get("experimentId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Date timestamp = Calendar.getInstance().getTime();

        // access to the array of experiment types (can only be done with a view context so
        // hardcoded in some other classes)
        // First is 'Count-Based'
        if (trialType.equals(Extras.COUNT_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_countbased_trial, null);
            Button addGeolocation = view.findViewById(R.id.count_addGeolocation_button);
            addGeolocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GeolocationActivity.class);
                    intent.putExtra("geolocation", geolocation);
                    startActivityForResult(intent, 1);
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if it's required and they do not add a location, do not write the trial to the db
                            if (trialBundle.getBoolean("geolocationRequired") && geolocation.getLocation() == null) {
                                Toast toast = new Toast(view.getContext());
                                toast.setText("Failed to create trial.\nGeolocation Required.");
                                toast.show();
                            } else {
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLatitude()));
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLongitude()));
                                UserDAL userDAL = new UserDAL();
                                Context context = view.getContext();
                                String experimenterId = userDAL.getDeviceUserId(context);
                                CountTrial trial = new CountTrial(UUID.randomUUID().toString(),
                                        experimenterId, geolocation, timestamp);
                                ExperimentDAL experimentDAL = new ExperimentDAL();
                                // use the dal to add the trial to the db
                                experimentDAL.addTrial(experimentId, trial);
                            }
                        }
                    }).create();
        // 'Binomial'
        } else if (trialType.equals(Extras.BINOMIAL_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_binomial_trial, null);
            // use this weird final one element array for access in the onClick function
            final boolean[] success = {true};
            Button successButton = view.findViewById(R.id.success_button);
            successButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (successButton.getText().equals("Success")) {
                        success[0] = false;
                        successButton.setText("Failure");
                    } else {
                        success[0] = true;
                        successButton.setText("Success");
                    }
                }
            });
            Button addGeolocation = view.findViewById(R.id.binomial_addGeolocation_button);
            addGeolocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GeolocationActivity.class);
                    intent.putExtra("geolocation", geolocation);
                    startActivityForResult(intent, 1);
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (trialBundle.getBoolean("geolocationRequired") && geolocation.getLocation() == null) {
                                Toast toast = new Toast(view.getContext());
                                toast.setText("Failed to create trial.\nGeolocation Required.");
                                toast.show();
                            }
                            else {
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLatitude()));
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLongitude()));
                                UserDAL userDAL = new UserDAL();
                                Context context = view.getContext();
                                String experimenterId = userDAL.getDeviceUserId(context);
                                BinomialTrial trial = new BinomialTrial(success[0],
                                        UUID.randomUUID().toString(), experimenterId, geolocation, timestamp);
                                ExperimentDAL experimentDAL = new ExperimentDAL();
                                experimentDAL.addTrial(experimentId, trial);
                            }
                        }
                    }).create();
        // 'Non-Negative Integer'
        } else if (trialType.equals(Extras.NONNEG_TYPE)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_nonnegative_trial, null);
            EditText amountEditText = view.findViewById(R.id.intamount_editText);
            Button addGeolocation = view.findViewById(R.id.nonneg_addGeolocation_button);
            addGeolocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GeolocationActivity.class);
                    intent.putExtra("geolocation", geolocation);
                    startActivityForResult(intent, 1);
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (trialBundle.getBoolean("geolocationRequired") && geolocation.getLocation() == null) {
                                Toast toast = new Toast(view.getContext());
                                toast.setText("Failed to create trial.\nGeolocation Required.");
                                toast.show();
                            }
                            else {
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLatitude()));
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLongitude()));
                                UserDAL userDAL = new UserDAL();
                                Context context = view.getContext();
                                String experimenterId = userDAL.getDeviceUserId(context);
                                NonNegTrial trial = new NonNegTrial(Integer.parseInt(String.valueOf(amountEditText.getText())),
                                        UUID.randomUUID().toString(),
                                        experimenterId, geolocation, timestamp);
                                ExperimentDAL experimentDAL = new ExperimentDAL();
                                experimentDAL.addTrial(experimentId, trial);
                            }
                        }
                    }).create();
        // 'Measurement'
        } else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_measurement_trial, null);
            EditText amountEditText = view.findViewById(R.id.amount_editText);
            Button addGeolocation = view.findViewById(R.id.measurement_addGeolocation_button);
            addGeolocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GeolocationActivity.class);
                    intent.putExtra("geolocation", geolocation);
                    startActivityForResult(intent, 1);
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Trial Type: " + trialType)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (trialBundle.getBoolean("geolocationRequired") && geolocation.getLocation() == null) {
                                Toast toast = new Toast(view.getContext());
                                toast.setText("Failed to create trial.\nGeolocation Required.");
                                toast.show();
                            }
                            else {
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLatitude()));
//                                Log.d("trial", String.valueOf(geolocation.getLocation().getLongitude()));
                                UserDAL userDAL = new UserDAL();
                                Context context = view.getContext();
                                String experimenterId = userDAL.getDeviceUserId(context);
                                MeasurementTrial trial = new MeasurementTrial(Double.parseDouble(String.valueOf(amountEditText.getText())),
                                        UUID.randomUUID().toString(),
                                        experimenterId, geolocation, timestamp);
                                ExperimentDAL experimentDAL = new ExperimentDAL();
                                experimentDAL.addTrial(experimentId, trial);
                            }
                        }
                    }).create();
        }
    }
}
