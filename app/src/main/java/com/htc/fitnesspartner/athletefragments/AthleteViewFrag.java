package com.htc.fitnesspartner.athletefragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.Coach;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.athleterecyclerviews.ClassViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class AthleteViewFrag extends Fragment {

    private RecyclerView recyclerView;
    ClassViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_athletes_fragment, container, false);
        recyclerView = view.findViewById(R.id.athlete_view);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        ArrayList<GroupClass> snaps = new ArrayList<>();
        adapter = new ClassViewAdapter(snaps);
        FirebaseFirestore.getInstance().collection("classes").whereArrayContains("athletes", Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if(e != null) {
                        Log.e("FB ERROR", e.getMessage().toString());
                    } else {
                        final GroupClass fillerClass = new GroupClass("You are not enrolled in any teams", "", System.currentTimeMillis(),
                                Collections.emptyList(), Collections.emptyList(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(queryDocumentSnapshots.isEmpty()) {
                            snaps.add(fillerClass);
                        } else {
                            snaps.remove(fillerClass);
                            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                snaps.add(ds.toObject(GroupClass.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

    }

}
