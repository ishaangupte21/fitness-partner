package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.htc.fitnesspartner.R;

import java.util.List;

public class InviteAthleteFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_fragment, container, false);

        final MaterialTextView textView = (MaterialTextView) view.findViewById(R.id.code_text_view);

        Bundle b = getArguments();

        name = b.getString("name");

        db.collection("classes").whereEqualTo("name", name).get().addOnSuccessListener(snapshots -> {
            List<DocumentSnapshot> docs = snapshots.getDocuments();
            DocumentSnapshot snap = docs.get(0);
            if (snap.exists()) {
                textView.setText(snap.getId());
            }

        }).addOnFailureListener(e -> Log.e("FB ERROR", e.getMessage().toString()));

        return view;
    }
}
