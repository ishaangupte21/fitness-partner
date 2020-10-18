package com.htc.fitnesspartner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UnenrollDialogTwo extends AppCompatDialogFragment {
    private UnenrollDialogTwo.UnenrollDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder b= new MaterialAlertDialogBuilder(getContext());

        b.setTitle("ALERT").setMessage("Are you sure you want to leave this class?\n This Action can NOT be undone")
                .setNegativeButton("No", (dialog, which) -> {

                }).setNeutralButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Yes", (dialog, which) -> {
                    listener.deleteClassTwo();
                });

        return b.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UnenrollDialogTwo.UnenrollDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement the listener");
        }
    }



    public interface UnenrollDialogListener{
        void deleteClassTwo();
    }
}
