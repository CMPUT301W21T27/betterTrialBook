package com.example.bettertrialbook.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.qr.CreateQRActivity;

/*
    Fragment for choosing an option after clicking on a trial/user. The owner can blacklist them
    which hides all their trials and doesn't allow them to add additional trials. Everyone can
    view someone else's profile
 */
public class TrialProfileFragment extends DialogFragment {
    private TrialProfileFragment.OnFragmentInteractionListener listener;
    private String experimenterID;
    private String experimentID;
    private Boolean isOwner = false;
    private Trial trial;
    private boolean isExperimenter = false;

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener {
        void onViewProfile();

        void onBlacklist(String experimenterID);
    }

    public TrialProfileFragment( Boolean isOwner, Trial trial, String experimentID) {
        super();
        this.trial = trial;
        this.experimenterID = trial.getExperimenterID();
        this.experimentID = experimentID;
        this.isOwner = isOwner;
        this.isExperimenter = this.experimenterID.equals(You.getUser().getID());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_trial_profile, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Trial Actions")
                .setItems(availableActions(), (dialog, which) -> onActionClick(which)).create();
    }

    private String[] availableActions() {
        if (isOwner) {
            if (isExperimenter) {
                return new String[]{"View Profile", "Create QR Code", "Block Trials", "Delete Trial"};
            }
            return new String[] { "View Profile", "Create QR Code", "Block Trials" };
        }
        if (isExperimenter) {
            return new String[]{"View Profile", "Create QR Code", "Delete Trial"};
        }
        return new String[]{"View Profile", "Create QR Code"};
    }

    private void onActionClick(int actionIndex) {
        if (actionIndex == 0)
            listener.onViewProfile();
        else if (actionIndex == 1)
            onCreateQRCodeClick();
        else if (actionIndex == 2)
            if (isOwner) {
                onBlacklistClick();
            } else {
                onDeleteClick();
            }
        else if (actionIndex == 3)
            onDeleteClick();
    }

    // delete the trial from the experiment
    private void onDeleteClick() {
        ExperimentDAL experimentDAL = new ExperimentDAL();
        experimentDAL.deleteTrial(experimentID, trial);
    }

    // blacklist the user from the experiment
    private void onBlacklistClick() {
        if (experimenterID.equals(You.getUser().getID())) {
            Toast.makeText(getActivity(), "You cannot blacklist yourself", Toast.LENGTH_LONG).show();

        } else {
            listener.onBlacklist(experimenterID);
        }
    }

    // create a qr code for the experiment
    private void onCreateQRCodeClick() {
        Intent intent = new Intent(getContext(), CreateQRActivity.class);
        intent.putExtra(Extras.TRIAL_ID, trial.getTrialID());
        intent.putExtra(Extras.EXPERIMENT_ID, experimentID);
        startActivity(intent);
    }
}
