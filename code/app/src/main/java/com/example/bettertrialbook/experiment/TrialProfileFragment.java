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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (isOwner) {
            return builder
                    .setView(view)
                    .setTitle("Profile Options")
                    .setNegativeButton("Block Trials", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            You you = new You();
                            if (experimenterID.equals(you.getUser().getID())) {
                                Toast.makeText(getActivity(), "You cannot blacklist yourself", Toast.LENGTH_LONG).show();

                            } else {
                                listener.onBlacklist(experimenterID);
                            }
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
