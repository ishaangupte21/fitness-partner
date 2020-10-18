package com.htc.fitnesspartner.athletefragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.athleterecyclerviews.AthleteClassLeaderboardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;

public class AthleteClassLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AthleteClassLeaderboardAdapter adapter;
    private String className;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.athlete_class_leaderboard_fragment, container, false);
        recyclerView = view.findViewById(R.id.athlete_class_leaderboard_recycler);
        className = getArguments().getString("class-name");
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        db.collection("userdata").document("accountNodes").collection("athletes").orderBy("completedSize", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        adapter = new AthleteClassLeaderboardAdapter(task.getResult().getDocuments());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e("FB ERROR", task.getException().getMessage().toString());
                    }
                });
    }
}
