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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;
import com.htc.fitnesspartner.athleterecyclerviews.AthleteClassWorkoutViewAdapter;

import java.util.List;

public class AthleteClassWorkoutViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private AthleteClassWorkoutViewAdapter adapter;
    private String className;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.athlete_view_workouts_fragment, container, false);
        recyclerView = view.findViewById(R.id.athlete_workouts_view_recycler);
        className = getArguments().getString("class-name");
        FirebaseFirestore.getInstance().collection("workouts").whereEqualTo("parentClass", className).orderBy("date", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                setUpRecyclerView(task.getResult().getDocuments());
            } else {
                Log.e("FIREBASE ERROR", task.getException().toString());
                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void setUpRecyclerView(List<DocumentSnapshot> docs) {
        adapter = new AthleteClassWorkoutViewAdapter(docs, getFragmentManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
