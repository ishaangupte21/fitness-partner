package com.htc.fitnesspartner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class UnenrollDialog extends AppCompatDialogFragment {
    private UnenrollDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder b= new MaterialAlertDialogBuilder(getContext());

        b.setTitle("ALERT").setMessage("Are you sure you want to leave this class?\n This Action can NOT be undone")
                .setNegativeButton("No", (dialog, which) -> {

                }).setNeutralButton("Cancel", (dialog, which) -> {})
                    .setPositiveButton("Yes", (dialog, which) -> {
                        listener.deleteClass();
                    });

        return b.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UnenrollDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement the listener");
        }
    }



    public interface UnenrollDialogListener{
        void deleteClass();
    }
}
