package com.example.bettertrialbook;

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

public class ConfirmationFragment extends DialogFragment {
    private String tag = "";
    private OnFragmentInteractionListener listener;

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener {
        void onOkPressedConfirm(String tag);
    }

    public ConfirmationFragment() {
        super();
    }

    public ConfirmationFragment(String tag) {
        super();
        if (tag.equals(""));
        this.tag = tag;
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
        confirmationText.setText(String.format("Are you sure you want to %s the experiment?", tag.toLowerCase()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Confirmation")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressedConfirm(tag);
                    }
                })
                .create();
    }
}