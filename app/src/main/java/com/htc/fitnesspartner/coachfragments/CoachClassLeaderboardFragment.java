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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.coachrecyclerviews.CoachLeaderboardAdapter;

public class CoachClassLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoachLeaderboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.athlete_class_leaderboard_fragment, container, false);
        recyclerView = v.findViewById(R.id.athlete_class_leaderboard_recycler);
        setUpRecyclerView();
        return v;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("userdata").document("accountNodes").collection("athletes")
                .orderBy("completedSize", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Athlete> options = new FirestoreRecyclerOptions.Builder<Athlete>().setQuery(query, Athlete.class).build();
        adapter = new CoachLeaderboardAdapter(options);
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
