package com.htc.fitnesspartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.htc.fitnesspartner.coachfragments.AddClassFragment;
import com.htc.fitnesspartner.coachfragments.ClassFragment;
import com.htc.fitnesspartner.coachfragments.DefaultCoachFrag;

import java.util.List;


public class CoachActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private CharSequence logOutId;
    private FrameLayout frameLayout;
    private Bundle savedState;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration[] listeners = new ListenerRegistration[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        savedState = savedInstanceState;

        navView = (NavigationView) findViewById(R.id.coachNavView);
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = (DrawerLayout) findViewById(R.id.coach_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.coach_toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.coach_frame_layout);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_bar_open, R.string.navigation_bar_close);

        drawerLayout.addDrawerListener(toggle);


        toggle.syncState();

        addMenuItemInNavDrawer();

        navView.setNavigationItemSelectedListener(this);

        final MenuItem home = navView.getMenu().add("Home");
        home.setIcon(R.drawable.ic_home_black_24dp);
        home.setOnMenuItemClickListener(item -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.coach_frame_layout, new DefaultCoachFrag()).commit();

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

//        home.setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.coach_frame_layout, new DefaultCoachFrag()).commit();




    }

    private void addMenuItemInNavDrawer(){

        Menu menu = navView.getMenu();



        final Menu submenu = menu.addSubMenu("Classes");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final MenuItem create = menu.add("Create Class");
        create.setIcon(R.drawable.ic_add_black_24dp);
        create.setOnMenuItemClickListener(item -> {

            getSupportFragmentManager().beginTransaction().replace(R.id.coach_frame_layout, new AddClassFragment()).commit();


            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        ListenerRegistration reg =  db.collection("userdata").document("accountNodes").collection("coaches").document(user.getUid())
                .addSnapshotListener((documentSnapshot, e) -> {

                    if(e != null){
                        Log.e("ERROR", e.getMessage().toString());
                        return;
                    }

                   if(documentSnapshot.exists()){
                       Coach coach = documentSnapshot.toObject(Coach.class);
                       List<String> coachClasses = coach.getClasses();
                        submenu.clear();
                       for(String id: coachClasses){

                           db.collection("classes").document(id).get().addOnSuccessListener(documentSnapshot1 -> {
                               final GroupClass gc = documentSnapshot1.toObject(GroupClass.class);

                               MenuItem item = submenu.add(gc.getName());
                               item.setOnMenuItemClickListener(item1 -> {

                                   Bundle args = new Bundle();

                                   args.putString("name", gc.getName());

                                   ClassFragment classFragment = new ClassFragment();
                                   classFragment.setArguments(args);

                                   getSupportFragmentManager().beginTransaction().replace(R.id.coach_frame_layout, classFragment).commit();

                                   drawerLayout.closeDrawer(GravityCompat.START);

                                   return false;
                               });
                           }).addOnFailureListener(e1 -> Log.e("ERROR", e1.getMessage().toString()));
                       }

                   }
                });

        listeners[0] = reg;





        MenuItem logoutBtn = menu.add("Log Out");
        logoutBtn.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        logOutId = logoutBtn.getTitle();


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals(logOutId)){
            mAuth.signOut();
            startActivity(new Intent(CoachActivity.this, MainActivity.class));
            finish();
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

       listeners[0].remove();

    }
}
