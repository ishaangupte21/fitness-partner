package com.htc.fitnesspartner;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.htc.fitnesspartner.athletefragments.AthleteClassFragment;
import com.htc.fitnesspartner.athletefragments.AthleteViewFrag;
import com.htc.fitnesspartner.athleterecyclerviews.ClassViewAdapter;
import com.htc.fitnesspartner.dialog.JoinClassDialog;
import com.htc.fitnesspartner.dialog.UnenrollDialog;

public class AthleteActivity extends AppCompatActivity implements JoinClassDialog.JoinClassDialogListener {

    private DrawerLayout layout;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CoordinatorLayout cLayout;
    private ListenerRegistration reg;
    private String adapterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        navView = (NavigationView) findViewById(R.id.athleteNavView);
        layout = findViewById(R.id.athlete_drawer);

        Toolbar toolbar = findViewById(R.id.athlete_toolbar);
        cLayout = findViewById(R.id.athlete_cood_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layout, toolbar, R.string.navigation_bar_open, R.string.navigation_bar_close);

        layout.addDrawerListener(toggle);

        toggle.syncState();

        addMenuToDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.athlete_frame_layout, new AthleteViewFrag()).commit();
        }

    }

    private void addMenuToDrawer() {
        FirebaseUser user = mAuth.getCurrentUser();

        final Menu menu = navView.getMenu();
        final Menu submenu = menu.addSubMenu("Classes");

        final MenuItem join = menu.add("Join a Class");

        join.setOnMenuItemClickListener(item -> {
            layout.closeDrawer(GravityCompat.START);
            JoinClassDialog dialog = new JoinClassDialog();
            dialog.show(getSupportFragmentManager(), "Join Class");


            return false;
        });


        reg = db.collection("userdata").document("accountNodes").collection("athletes").document(user.getUid()).
                addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e("ERROR", e.getMessage().toString());
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        Athlete a = documentSnapshot.toObject(Athlete.class);
                        submenu.clear();
                        for (String i : a.getEnrolledClasses()) {
                            db.collection("classes").document(i).get().addOnSuccessListener(documentSnapshot1 -> {
                                final GroupClass gc = documentSnapshot1.toObject(GroupClass.class);
                                MenuItem item = submenu.add(gc.getName());
                                item.setOnMenuItemClickListener(item1 -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("class-name", gc.getName());
                                    AthleteClassFragment fragment = new AthleteClassFragment();
                                    fragment.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.athlete_frame_layout, fragment).commit();
                                    layout.closeDrawer(GravityCompat.START);
                                    return false;
                                });
                            }).addOnFailureListener(e1 -> Log.e("FB ERROR", e1.getMessage().toString()));
                        }
                    }
                });


        MenuItem logOut = menu.add("Log Out");
        logOut.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        logOut.setOnMenuItemClickListener(item -> {

            mAuth.signOut();
            startActivity(new Intent(AthleteActivity.this, MainActivity.class));
            finish();
            return false;
        });

    }

    @Override
    public void onBackPressed() {
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void joinClass(final String id) {

        FunctionsInterface.addAthleteToClass(FirebaseAuth.getInstance().getCurrentUser().getUid(), id).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception e = task.getException();
                if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    Object details = ffe.getDetails();
                    Log.e("Functions Exception", details.toString());
                }
            } else {

                Snackbar s = Snackbar.make(cLayout, task.getResult().toString(), Snackbar.LENGTH_SHORT);

                s.show();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        reg.remove();
    }



}
