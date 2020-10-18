package com.htc.fitnesspartner.coachrecyclerviews;

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

public class IndividualWorkoutAthletesAdapter extends FirestoreRecyclerAdapter<Athlete, IndividualWorkoutAthletesAdapter.Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public IndividualWorkoutAthletesAdapter(@NonNull FirestoreRecyclerOptions<Athlete> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Athlete model) {
        holder.athleteNameView.setText(model.getFirstName() + " " + model.getLastName());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_workout_adapter, parent, false);
        return new Holder(v);
    }

    public class Holder extends RecyclerView.ViewHolder {

        private MaterialTextView athleteNameView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            athleteNameView = itemView.findViewById(R.id.indiviual_workout_athlete_name);
        }
    }
}
