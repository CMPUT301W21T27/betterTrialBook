package com.example.bettertrialbook;

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

public class EditContactFragment extends DialogFragment {
    private EditText email;
    private EditText phone;
    private OnFragmentInteractionListener listener;

    /* Ok pressed interface */
    public interface OnFragmentInteractionListener{
        void onOkPressed(String email, String phone);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_contact_fragment,null);
        EditText emailInput = view.findViewById(R.id.email_editText);
        EditText phoneInput = view.findViewById(R.id.phone_editText);



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Contact Info")

                .setNegativeButton("Cancel",null)
                //exit fragment

                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailInput.getText().toString();
                        String phone = phoneInput.getText().toString();

                        listener.onOkPressed(email,phone);
                    }
                })
                .create();
    }
}
