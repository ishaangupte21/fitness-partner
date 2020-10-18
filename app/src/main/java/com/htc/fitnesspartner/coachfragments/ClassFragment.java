package com.htc.fitnesspartner.coachfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.htc.fitnesspartner.R;

public class ClassFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static String name1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_fragment, container, false);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.class_frame);

        MaterialTextView name = (MaterialTextView) view.findViewById(R.id.class_name_view);

        Bundle nameBundle = getArguments();
         name1 = nameBundle.getString("name");

        name.setText(name1);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.class_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
            ClassDashboardFragment frag3 = new ClassDashboardFragment();
            Bundle arg4 = new Bundle();
            arg4.putString("class-name", name1);
            frag3.setArguments(arg4);
            getChildFragmentManager().beginTransaction().replace(R.id.class_frame, frag3).commit();

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment selectedFragment = null;
        switch (item.getItemId()){
            case R.id.add_athlete :
                InviteAthleteFragment fragment = new InviteAthleteFragment();
                Bundle args = new Bundle();
                args.putString("name", name1);
                fragment.setArguments(args);
                getChildFragmentManager().beginTransaction().replace(R.id.class_frame, fragment).commit();
                break;
            case R.id.add_workout:
                AddWorkoutFragment frag = new AddWorkoutFragment();
                Bundle arg2 = new Bundle();
                arg2.putString("name", name1);
                frag.setArguments(arg2);
                getChildFragmentManager().beginTransaction().replace(R.id.class_frame, frag).commit();
                break;
            case R.id.view_workouts:
                CoachViewWorkoutsFragment frag1 = new CoachViewWorkoutsFragment(getChildFragmentManager());
                Bundle args1 = new Bundle();
                args1.putString("name", name1);
                frag1.setArguments(args1);
                getChildFragmentManager().beginTransaction().replace(R.id.class_frame, frag1).commit();
                break;
            case R.id.view_leaderboard:
                CoachClassLeaderboardFragment frag2 = new CoachClassLeaderboardFragment();
                Bundle arg3 = new Bundle();
                arg3.putString("class-name", name1);
                frag2.setArguments(arg3);
                getChildFragmentManager().beginTransaction().replace(R.id.class_frame, frag2).commit();
                break;
            case R.id.view_class:
                ClassDashboardFragment frag3 = new ClassDashboardFragment();
                Bundle arg4 = new Bundle();
                arg4.putString("class-name", name1);
                frag3.setArguments(arg4);
                getChildFragmentManager().beginTransaction().replace(R.id.class_frame, frag3).commit();
                break;
        }

        return true ;
    }



    public static String getname1(){
        return name1;
    }

}
