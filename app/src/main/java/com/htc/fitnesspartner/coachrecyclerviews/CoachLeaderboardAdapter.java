package com.htc.fitnesspartner.coachrecyclerviews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.athleterecyclerviews.AthleteClassLeaderboardAdapter;

public class CoachLeaderboardAdapter extends FirestoreRecyclerAdapter<Athlete, CoachLeaderboardAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CoachLeaderboardAdapter(@NonNull FirestoreRecyclerOptions<Athlete> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Athlete model) {
        int rank = position + 1;
        holder.positionTextView.setText(String.valueOf(rank));
        holder.nameTextView.setText(model.getFirstName() + " " + model.getLastName());
        holder.amountCompletedTextView.setText(model.getWorkoutsCompleted().size() + " Workouts Completed");
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.athlete_leaderboard_adapter, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        MaterialTextView positionTextView, nameTextView, amountCompletedTextView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.athlete_leaderboard_position);
            nameTextView = itemView.findViewById(R.id.athlete_leaderboard_name);
            amountCompletedTextView = itemView.findViewById(R.id.athlete_leaderboard_workouts_completed);
        }
    }
}
