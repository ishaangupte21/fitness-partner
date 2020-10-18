package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class AddClassFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CoordinatorLayout layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_class_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        layout = view.findViewById(R.id.addClassLayout);

        Button addBtn = (Button) view.findViewById(R.id.create_class_btn);

        final TextInputEditText nameEditText, descEditText;

            nameEditText = (TextInputEditText) view.findViewById(R.id.add_class_editText);
            descEditText = (TextInputEditText) view.findViewById(R.id.class_desc_editText);



        addBtn.setOnClickListener(v -> {

            String name = nameEditText.getText().toString().trim();
            String desc = descEditText.getText().toString().trim();

            GroupClass groupClass = new GroupClass(name, desc, System.currentTimeMillis(), new ArrayList<>(), new ArrayList<>(), FirebaseAuth.getInstance().getCurrentUser().getUid());

            db.collection("classes").add(groupClass).addOnSuccessListener(documentReference -> db.collection("userdata").document("accountNodes").collection("coaches")
                    .document(user.getUid()).update("classes", FieldValue.arrayUnion(documentReference.getId())).addOnSuccessListener(aVoid -> {
                        Snackbar snackbar = Snackbar.make(layout, "Class Created Successfully", Snackbar.LENGTH_SHORT)
                                .setAction("UNDO", v1 -> db.collection("classes").document(documentReference.getId()).delete().addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        db.collection("userdata").document("accountNodes").collection("coaches")
                                                .document(user.getUid()).update("classes", FieldValue.arrayRemove(documentReference.getId())).addOnSuccessListener(aVoid1 -> {
                                                    Snackbar snackbar1 = Snackbar.make(layout, "Undo Successful", Snackbar.LENGTH_SHORT);

                                                    snackbar1.show();
                                                }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show());
                                    }else{
                                        Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }));
                        snackbar.show();
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show());
        });

        return view;

    }
}
