package com.htc.fitnesspartner.coachrecyclerviews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.htc.fitnesspartner.Coach;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CoachWorkoutViewAdapter extends FirestoreRecyclerAdapter<Workout, CoachWorkoutViewAdapter.Holder> {

    private FragmentManager fragmentManager;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CoachWorkoutViewAdapter(@NonNull FirestoreRecyclerOptions<Workout> options, FragmentManager fragmentManager) {
        super(options);
        this.fragmentManager = fragmentManager;
    }



    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Workout model) {
        holder.workoutNameView.setText(model.getName());
        holder.workoutDateview.setText(getDateFromTime(model.getDate()));
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_workout_view_adapter, parent, false);
        return new Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private MaterialTextView workoutNameView, workoutDateview;
        public Holder(@NonNull View itemView) {
            super(itemView);
            workoutNameView = itemView.findViewById(R.id.coach_workout_title);
            workoutDateview = itemView.findViewById(R.id.coach_workout_date);
        }
    }

    private String getDateFromTime(Long time) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
    }
}
