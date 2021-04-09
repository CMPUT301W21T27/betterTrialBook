/*
    fragment to confirm an action
    Generally finished
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.R;

/**
 * Fragment for user to confirm whatever "serious" action they are taking
 * (ending/publishing/blacklisting etc)
 */
public class ConfirmationFragment extends DialogFragment {
    private String tag = "";
    private String experimenterID = "";
    private OnFragmentInteractionListener listener;
    private Boolean blacklist = false;

    /**
     * Ok pressed interface returning a tag corresponding to which confirmation it was for
     */
    public interface OnFragmentInteractionListener {
        void onOkPressedConfirm(String tag, String experimenterID);
    }

    public ConfirmationFragment() {
        super();
    }

    public ConfirmationFragment(String tag, Boolean blacklist, String experimenterID) {
        super();
        this.blacklist = blacklist;
        this.tag = tag;
        this.experimenterID = experimenterID;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_confirmation,null);
        TextView confirmationText = view.findViewById(R.id.confirmation_text);
        if (tag.equals("Subscribe")) {
            tag += " to";
        } else if (tag.equals("Unsubscribe")) {
            tag += " from";
        }

        if (blacklist) {
            confirmationText.setText(String.format("Are you sure you want to %s this experimenter's trials?", tag.toLowerCase()));
        } else {
            confirmationText.setText(String.format("Are you sure you want to %s the experiment?", tag.toLowerCase()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressedConfirm(tag, experimenterID);
                    }
                })
                .create();
    }
}