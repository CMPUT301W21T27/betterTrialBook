package com.example.bettertrialbook.profile;

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

/**
 * Fragment for entering new contact info
 */
public class EditContactFragment extends DialogFragment {
    private String email = "";
    private String phone = "";
    private OnFragmentInteractionListener listener;

    public EditContactFragment() {
        super();
    }

    public EditContactFragment(String email, String phone) {
        super();
        this.email = email;
        this.phone = phone;
    };

    /**
     * Ok pressed interface after collecting email and phone from user registration
     */
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

        emailInput.setText(email);
        phoneInput.setText(phone);

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
