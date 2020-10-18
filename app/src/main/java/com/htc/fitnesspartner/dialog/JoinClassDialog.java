package com.htc.fitnesspartner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.htc.fitnesspartner.R;

public class JoinClassDialog extends AppCompatDialogFragment {

    private TextInputEditText codeEditText;
    JoinClassDialogListener listener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context;
        MaterialAlertDialogBuilder b = new MaterialAlertDialogBuilder(getContext());

//        ViewGroup parent;
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.join_class_dialog, null);

        codeEditText = view.findViewById(R.id.class_code_editText);

        b.setView(view).setTitle("Join a Class").setNegativeButton("cancel", (dialog, which) -> {

        }).setPositiveButton("Join", (dialog, which) -> {
            String id = codeEditText.getText().toString().trim();
            listener.joinClass(id);
        });

        return b.create();


    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (JoinClassDialogListener) context;
        } catch (ClassCastException e ) {
            throw new ClassCastException(context.toString()+" Must implement the listener");
        }

    }


    public interface JoinClassDialogListener{
        void joinClass(String id);
    }



}
