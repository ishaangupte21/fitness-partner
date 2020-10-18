package com.htc.fitnesspartner.athletefragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AthleteIndividualWorkoutFragment extends Fragment {
    private Workout workout;
    private String workoutName, workoutId;
    private MaterialTextView workoutNameView, workoutDateView, workoutExerciseView;
    private MaterialButton completeButton;
    private boolean isComplete;
    private ProgressBar spinner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.athlete_individual_workout_fragment, container, false);
        workoutName = getArguments().getString("workout-name");
        workoutNameView = view.findViewById(R.id.athlete_individual_workout_name);
        workoutDateView = view.findViewById(R.id.athlete_individual_workout_due_date);
        workoutExerciseView = view.findViewById(R.id.athlete_individual_workout_exercise);
        completeButton = view.findViewById(R.id.athlete_individual_workout_complete_button);
        spinner = view.findViewById(R.id.complete_button_spinner);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("workouts").whereEqualTo("name", workoutName).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(e != null) {
                Log.e("FB ERROR", e.getMessage().toString());
            } else {
                if(queryDocumentSnapshots.getDocuments().size() != 0) {
                    DocumentSnapshot workoutSnap = queryDocumentSnapshots.getDocuments().get(0);
                    workoutId = workoutSnap.getId();
                    Workout workout = workoutSnap.toObject(Workout.class);
                    workoutNameView.setText(workout.getName());
                    workoutDateView.setText("Due by: " + getDateFromTime(workout.getDate()));
                    if(workout.getDate() < System.currentTimeMillis()) {
                        workoutDateView.setTextColor(Color.rgb(235, 52, 82));
                    }
                    workoutExerciseView.setText(workout.getAmount() + " " + workout.getExercise());
                    if(workout.getCompletedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        isComplete = true;
                        completeButton.setText("Mark as Incomplete");
                    } else {
                        isComplete = false;
                        completeButton.setText("Mark as Complete");
                    }
                }
            }
        });

        completeButton.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            if(isComplete) {
                markWorkoutIncomplete().addOnCompleteListener(task -> {
                    spinner.setVisibility(View.GONE);
                    if(task.isSuccessful()) {
                        Toast.makeText(getContext(), "Workout marked as Incomplete", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                    }
                });
            } else {
                markWorkoutAsComplete().addOnCompleteListener(task -> {
                    spinner.setVisibility(View.GONE);
                    if(task.isSuccessful()) {
                        Toast.makeText(getContext(), "Workout marked as Complete", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        return view;
    }

    private String getDateFromTime(Long time) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
    }


    private Task markWorkoutAsComplete() {
        Map<String, Object> data = new HashMap<>();
        data.put("workoutId", workoutId);
        return FirebaseFunctions.getInstance().getHttpsCallable("completeWorkout").call(data).continueWith(task -> task.getResult().getData());
    }

    private Task markWorkoutIncomplete() {
        Map<String, Object> data = new HashMap<>();
        data.put("workoutId", workoutId);
        return FirebaseFunctions.getInstance().getHttpsCallable("workoutIncomplete").call(data).continueWith(task -> task.getResult().getData());
    }
}
