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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.coachrecyclerviews.DefaultRViewAdapter;

public class DefaultCoachFrag extends Fragment {
    private DefaultRViewAdapter adapter;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_class_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.default_recycler_view);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v("FIREBASEUID", uid);
        Query query = FirebaseFirestore.getInstance().collection("classes").whereEqualTo("coach", uid);
        FirestoreRecyclerOptions<GroupClass> options = new FirestoreRecyclerOptions.Builder<GroupClass>().setQuery(query, GroupClass.class).build();
        adapter = new DefaultRViewAdapter(options);
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
