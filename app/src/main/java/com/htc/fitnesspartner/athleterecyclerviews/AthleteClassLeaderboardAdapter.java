package com.htc.fitnesspartner.athleterecyclerviews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.R;

import java.util.List;

public class AthleteClassLeaderboardAdapter extends RecyclerView.Adapter<AthleteClassLeaderboardAdapter.Holder> {

    private List<DocumentSnapshot> athletesSnaps;

    public AthleteClassLeaderboardAdapter(List<DocumentSnapshot> athletes) {
        this.athletesSnaps = athletes;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.athlete_leaderboard_adapter, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Athlete athlete = athletesSnaps.get(position).toObject(Athlete.class);
        int rank = position + 1;
        holder.positionTextView.setText(String.valueOf(rank));
        holder.nameTextView.setText(athlete.getFirstName() + " " + athlete.getLastName());
        holder.amountCompletedTextView.setText(athlete.getWorkoutsCompleted().size() + " Workouts Completed");
    }

    @Override
    public int getItemCount() {
        return athletesSnaps.size();
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
