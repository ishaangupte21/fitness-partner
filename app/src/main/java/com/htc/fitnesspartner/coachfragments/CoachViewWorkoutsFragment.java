package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;
import com.htc.fitnesspartner.coachrecyclerviews.CoachWorkoutViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoachViewWorkoutsFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private RecyclerView recyclerView;
    private CoachWorkoutViewAdapter adapter;
    private FragmentManager fragmentManager;

    public CoachViewWorkoutsFragment(FragmentManager manager) {
        super();
        fragmentManager = manager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        List<Workout> workoutList = new ArrayList<>();
        View view = inflater.inflate(R.layout.coach_view_workouts_fragment, container, false);
        recyclerView = view.findViewById(R.id.coach_workouts_view_recycler);
        name = getArguments().getString("name");
        setUpRecyclerView();
        return view;

    }

    private void setUpRecyclerView() {
        Query query = db.collection("workouts").whereEqualTo("parentClass", name).orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Workout> options = new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(query, Workout.class).build();
        adapter = new CoachWorkoutViewAdapter(options, fragmentManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
