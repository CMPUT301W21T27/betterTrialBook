package com.example.bettertrialbook.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bettertrialbook.R;

/*
* Fragment called when either:
*   No username entered
*   Username entered is already taken
* Title of fragment changes depending on which reason it was called for
* */
public class InvalidUsernameFragment extends DialogFragment {
    private String message;
    private OnFragmentInteractionListener listener;

    public InvalidUsernameFragment(String message) {
        this.message = message;
    }

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener{
        void onOkPressed();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    +" must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.invalid_username_fragment,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(message)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkPressed();
                    }
                })
                .create();
    }
}
