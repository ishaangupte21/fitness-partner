package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.coachrecyclerviews.CoachClassDashboardAdapter;

public class ClassDashboardFragment extends Fragment {
    private RecyclerView recyclerView;

    private CoachClassDashboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coach_class_dashboard, container, false);
        recyclerView = v.findViewById(R.id.coach_dashboard_recycler);
        String className = getArguments().getString("class-name");
        FirebaseFirestore.getInstance().collection("classes").whereEqualTo("name", className).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.v("QUERY_SIZE", String.valueOf(task.getResult().size()));
                DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                setUpRecyclerView(ds.getId());
            } else {
                Log.e("FB-ERROR", task.getException().getMessage().toString());
            }
        });
        return v;
    }

    private void setUpRecyclerView(String classId) {
        Query query = FirebaseFirestore.getInstance().collection("userdata").document("accountNodes").collection("athletes")
                .whereArrayContains("classes", classId).orderBy("lastName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Athlete> options = new FirestoreRecyclerOptions.Builder<Athlete>().setQuery(query, Athlete.class).build();
        adapter = new CoachClassDashboardAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
