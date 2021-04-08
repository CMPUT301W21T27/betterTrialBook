/*
 * Fragment for editing experiment details like description or region
 * */
package com.example.bettertrialbook.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.R;

public class EditExperimentFragment extends DialogFragment {
    private String description = "";
    private String region = "";
    private EditExperimentFragment.OnFragmentInteractionListener listener;

    public EditExperimentFragment() {
        super();
    }

    public EditExperimentFragment(String description, String region) {
        super();
        this.description = description;
        this.region = region;
    };

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener{
        void onOkPressed(String description, String region, Boolean delete);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof EditExperimentFragment.OnFragmentInteractionListener){
            listener = (EditExperimentFragment.OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    +" must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_experiment,null);
        EditText descriptionInput = view.findViewById(R.id.descriptionEdit_editText);
        EditText regionInput = view.findViewById(R.id.regionEdit_editText);

        descriptionInput.setText(description);
        regionInput.setText(region);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Details or Delete Experiment")

                .setNegativeButton("DELETE EXPERIMENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressed("", "", true);
                    }
                })

                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String description = descriptionInput.getText().toString();
                        String region = regionInput.getText().toString();

                        listener.onOkPressed(description, region, false);
                    }
                })
                .create();
    }
}

