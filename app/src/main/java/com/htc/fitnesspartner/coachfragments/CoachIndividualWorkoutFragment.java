package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.coachrecyclerviews.IndividualWorkoutAthletesAdapter;

public class CoachIndividualWorkoutFragment extends Fragment {

    private MaterialTextView classNameView;
    private RecyclerView athleteRecyclerView;
    private IndividualWorkoutAthletesAdapter adapter;
    private String workoutName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_individual_fragment, container, false);
        classNameView = view.findViewById(R.id.individual_workout_class_name);
        athleteRecyclerView = view.findViewById(R.id.individual_workout_athlete_recycler_view);
        workoutName = getArguments().getString("workout-name");
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("userdata").document("accountNodes").collection("athletes")
                .whereArrayContains("workoutsCompleted", workoutName);
        FirestoreRecyclerOptions<Athlete> options = new FirestoreRecyclerOptions.Builder<Athlete>().setQuery(query, Athlete.class).build();
        adapter = new IndividualWorkoutAthletesAdapter(options);
        athleteRecyclerView.setHasFixedSize(true);
        athleteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        athleteRecyclerView.setAdapter(adapter);
    }
}
