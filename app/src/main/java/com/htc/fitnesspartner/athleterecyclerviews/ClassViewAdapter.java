package com.htc.fitnesspartner.athleterecyclerviews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.htc.fitnesspartner.Athlete;
import com.htc.fitnesspartner.FunctionsInterface;
import com.htc.fitnesspartner.GroupClass;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.dialog.UnenrollDialog;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class ClassViewAdapter extends RecyclerView.Adapter<ClassViewAdapter.Holder> implements PopupMenu.OnMenuItemClickListener {

    private List<GroupClass> classes;

    public ClassViewAdapter(List<GroupClass> classes) {
        super();
        this.classes = classes;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.athlete_view_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        GroupClass groupClass = classes.get(position);
        holder.nameView.setText(groupClass.getName());
        if (groupClass.getName().toLowerCase().equals("you are not enrolled in any teams")) {
            holder.optionsButton.setVisibility(View.GONE);
        }
        holder.optionsButton.setOnClickListener(this::showPopup);
    }


    @Override
    public int getItemCount() {
        return classes.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.athlete_view_class_workouts:
                return true;
            case R.id.athlete_view_class_leaderboard:
                return true;
            case R.id.remove_class:
                return true;
            default:
                return false;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        MaterialTextView nameView;
        ImageButton optionsButton;

        public Holder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.athlete_textview);
            optionsButton = itemView.findViewById(R.id.img_more_btn);
        }
    }

    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.athlete_class_menu);
        popupMenu.show();
    }
}

