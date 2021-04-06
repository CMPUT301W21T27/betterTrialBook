/*
    Fragment for choosing an option after clicking on a trial/user. The owner can blacklist them
    which hides all their trials and doesn't allow them to add additional trials. Everyone can
    view someone else's profile
 */
package com.example.bettertrialbook.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.models.Trial;
import com.example.bettertrialbook.qr.CreateQRActivity;

public class TrialProfileFragment extends DialogFragment {
    private TrialProfileFragment.OnFragmentInteractionListener listener;
    private String experimenterID = "";
    private String experimentID = "";
    private Boolean isOwner = false;
    private Trial trial;

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener {
        void onViewProfile();

        void onBlacklist(String experimenterID);

    }

    public TrialProfileFragment(Boolean isOwner, Trial trial) {
        super();
        this.trial = trial;
        this.isOwner = isOwner;
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
            return new String[] { "View Profile", "Create QR Code", "Block Trials" };
        }
        return new String[] { "View Profile", "Create QR Code" };
    }

    private void onActionClick(int actionIndex) {
        if (actionIndex == 0)
            listener.onViewProfile();
        else if (actionIndex == 1)
            onCreateQRCodeClick();
        else if (actionIndex == 2)
            onBlacklistClick();
    }

    private void onBlacklistClick() {
        if (experimenterID.equals(You.getUser().getID())) {
            Toast.makeText(getActivity(), "You cannot blacklist yourself", Toast.LENGTH_LONG).show();

        } else {
            listener.onBlacklist(experimenterID);
        }
    }

    private void onCreateQRCodeClick() {
        Intent intent = new Intent(getContext(), CreateQRActivity.class);
        intent.putExtra(Extras.TRIAL, trial);
        intent.putExtra(Extras.EXPERIMENT_ID, experimentID);
        startActivity(intent);
    }
}
