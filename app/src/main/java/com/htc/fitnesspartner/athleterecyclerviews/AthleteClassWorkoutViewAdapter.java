package com.htc.fitnesspartner.athleterecyclerviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;
import com.htc.fitnesspartner.athletefragments.AthleteIndividualWorkoutFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AthleteClassWorkoutViewAdapter extends RecyclerView.Adapter<AthleteClassWorkoutViewAdapter.Holder> {

    private List<DocumentSnapshot> workoutSnapshots;
    private FragmentManager fragmentManager;

    public AthleteClassWorkoutViewAdapter(List<DocumentSnapshot> workoutSnapshots, FragmentManager fragmentManager) {
        this.workoutSnapshots = workoutSnapshots;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.athlete_workout_view_adapter, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        DocumentSnapshot ds = this.workoutSnapshots.get(position);
        Workout workout = ds.toObject(Workout.class);
        holder.workoutTitleView.setText(workout.getName());
        holder.workoutDateView.setText(getDateFromTime(workout.getDate()));
        holder.itemView.setOnClickListener(v -> {
            AthleteIndividualWorkoutFragment fragment = new AthleteIndividualWorkoutFragment();
            Bundle bundle = new Bundle();
            bundle.putString("workout-name", workout.getName());
            fragment.setArguments(bundle);
            this.fragmentManager.beginTransaction().replace(R.id.athlete_class_frame, fragment).commit();
        });

    }

    @Override
    public int getItemCount() {
        return workoutSnapshots.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        MaterialTextView workoutTitleView, workoutDateView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            workoutTitleView = itemView.findViewById(R.id.athlete_workout_title);
            workoutDateView = itemView.findViewById(R.id.athlete_workout_date);
        }
    }

    private String getDateFromTime(Long time) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
    }
}
