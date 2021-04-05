package com.example.bettertrialbook.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.UserDAL;
import com.example.bettertrialbook.models.User;

public class TrialProfileFragment extends DialogFragment {
    private TrialProfileFragment.OnFragmentInteractionListener listener;
    private String experimenterID = "";
    private String experimentID = "";
    private Boolean isOwner = false;

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener {
        void onViewProfile();
        void onBlacklist(String experimenterID);
    }

    public TrialProfileFragment(String experimenterID, String experimentID, Boolean isOwner) {
        super();
        this.experimenterID = experimenterID;
        this.isOwner = isOwner;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    +" must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_trial_profile,null);
        TextView fragmentText = view.findViewById(R.id.trial_profile);

        // maybe add the current selected userID/username

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (isOwner) {
            return builder
                    .setView(view)
                    .setTitle("Profile Options")
                    .setNegativeButton("Block Trials", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onBlacklist(experimenterID);
                        }
                    })
                    .setPositiveButton("View Profile", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onViewProfile();
                        }
                    })
                    .create();
        } else {
            return builder
                    .setView(view)
                    .setTitle("Profile Options")
                    .setPositiveButton("View Profile", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onViewProfile();
                        }
                    })
                    .create();
        }
    }
}
