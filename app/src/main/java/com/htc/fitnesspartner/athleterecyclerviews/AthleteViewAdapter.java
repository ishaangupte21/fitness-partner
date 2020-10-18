package com.htc.fitnesspartner.athleterecyclerviews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.coachfragments.ClassFragment;

import java.util.Collections;
import java.util.List;

public class AthleteViewAdapter extends FirestoreRecyclerAdapter<GroupClass, AthleteViewAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Athlete athlete;
    private String className;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AthleteViewAdapter(@NonNull FirestoreRecyclerOptions<GroupClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull GroupClass model) {
        if(ClassFragment.getname1() != null){
            className = ClassFragment.getname1();

            db.collection("classes").whereEqualTo("name", className).get().addOnSuccessListener(snapshots -> {
                DocumentSnapshot snapshot = snapshots.getDocuments().get(0);
                if(snapshot != null){
                    GroupClass gc = snapshot.toObject(GroupClass.class);

                    List<String> athletes = gc.getAthletes();

                    Collections.sort(athletes);

                    for(String i : athletes){
                        db.collection("userdata").document("accountNodes").collection("athletes").document(i).get().addOnSuccessListener(documentSnapshot -> {
                            Athlete a = documentSnapshot.toObject(Athlete.class);

                            String name = a.getFirstName() + " " + a.getLastName();

                            holder.textView.setText(name);

                        }).addOnFailureListener(e -> Log.e("FIREBASE ERROR", e.getMessage().toString()));
                    }


                }

            }).addOnFailureListener(e -> Log.e("FIREBASE ERROR", e.getMessage().toString()));


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
       View view =  inflater.inflate(R.layout.athlete_view_layout, parent,false);

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (MaterialTextView) itemView.findViewById(R.id.athlete_textview);

        }
    }
}
