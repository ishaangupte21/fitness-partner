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

public class CoachClassDashboardAdapter extends FirestoreRecyclerAdapter<Athlete, CoachClassDashboardAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CoachClassDashboardAdapter(@NonNull FirestoreRecyclerOptions<Athlete> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Athlete model) {
        holder.athleteNameView.setText(model.getFirstName() + " " + model.getLastName());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_dashboard_adapter, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        MaterialTextView athleteNameView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            athleteNameView = itemView.findViewById(R.id.coach_dashboard_athlete_name);
        }
    }
}
