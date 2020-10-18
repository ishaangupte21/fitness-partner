package com.htc.fitnesspartner.athletefragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.functions.FirebaseFunctions;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.AthleteActivity;
import com.htc.fitnesspartner.MainActivity;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.dialog.UnenrollDialog;
import com.htc.fitnesspartner.dialog.UnenrollDialogTwo;

import java.util.HashMap;
import java.util.Map;

public class AthleteClassFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    private MaterialTextView classNameView;
    private FrameLayout frameLayout;
    private BottomNavigationView navigationView;
    private String className;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.athlete_class_fragment, container, false);
        className = getArguments().getString("class-name");
        classNameView = view.findViewById(R.id.athlete_class_name_view);
        frameLayout = view.findViewById(R.id.athlete_class_frame);
        navigationView = view.findViewById(R.id.athlete_class_bottom_nav);
        coordinatorLayout = view.findViewById(R.id.athlete_class_coordinator_layout);
        progressBar = view.findViewById(R.id.athlete_class_fragment_progress_bar);
        navigationView.setOnNavigationItemSelectedListener(this);

        classNameView.setText(className);

        if (savedInstanceState == null) {
            goToViewWorkoutFragment();
        }

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.athlete_view_class_workouts:
                goToViewWorkoutFragment();
                break;
            case R.id.athlete_view_class_leaderboard:
                AthleteClassLeaderboardFragment fragment = new AthleteClassLeaderboardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("class-name", className);
                fragment.setArguments(bundle);
                getChildFragmentManager().beginTransaction().replace(R.id.athlete_class_frame, fragment).commit();
                break;
            case R.id.remove_class:
                initiateLeaveClass();
                return false;
        }
        return true;
    }

    private void goToViewWorkoutFragment() {
        AthleteClassWorkoutViewFragment viewWorkoutFragment = new AthleteClassWorkoutViewFragment();
        Bundle args = new Bundle();
        args.putString("class-name", this.className);
        viewWorkoutFragment.setArguments(args);
        getChildFragmentManager().beginTransaction().replace(R.id.athlete_class_frame, viewWorkoutFragment).commit();
    }

    private void initiateLeaveClass() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Are you sure you want to leave this class?", Snackbar.LENGTH_SHORT);
        snackbar.setAction("YES", v -> {
            progressBar.setVisibility(View.VISIBLE);
            leaveClass().addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if(!task.isSuccessful()) {
                    Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                } else {
                    if(!task.getResult().toString().equals("finished successfully")) {
                        Toast.makeText(getContext(), task.getResult().toString(), Toast.LENGTH_SHORT);
                    } else {
                        Snackbar.make(coordinatorLayout, "Successfully Unenrolled", Snackbar.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.athlete_frame_layout, new AthleteViewFrag()).commit();
                    }
                }
            });
        });
        snackbar.show();
    }



    private Task leaveClass() {
        Map<String, Object> data = new HashMap<>();
        data.put("className", className);
        return FirebaseFunctions.getInstance().getHttpsCallable("unenrollFromClass").call(data).continueWith(task -> task.getResult().getData());
    }


}
