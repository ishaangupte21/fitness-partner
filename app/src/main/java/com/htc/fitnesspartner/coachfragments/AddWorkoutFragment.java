package com.htc.fitnesspartner.coachfragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.htc.fitnesspartner.FunctionsInterface;
import com.htc.fitnesspartner.R;
import com.htc.fitnesspartner.Workout;

import java.util.ArrayList;
import java.util.Calendar;

public class AddWorkoutFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private TextInputEditText title, exercise, amount, reps;
    private MaterialButton date, addWorkout;
    private long time;
    private CoordinatorLayout cLayout;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.add_workout_fragment, null);
        cLayout = view.findViewById(R.id.add_wkt_cLayout);
        title = view.findViewById(R.id.workout_title_editText);
        exercise = view.findViewById(R.id.exercise_editText);
        amount = view.findViewById(R.id.amount_input);
        reps = view.findViewById(R.id.reps_input);

        date = view.findViewById(R.id.pick_date_btn);
        addWorkout = view.findViewById(R.id.add_workout_btn);

        date.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        addWorkout.setOnClickListener(v -> {
            addWorkout(title.getText().toString(), exercise.getText().toString(), amount.getText().toString(),reps.getText().toString() );
        });
        name = ClassFragment.getname1();
        Log.d("NAME", name);

        return view;
    }



    private void showDatePickerDialog(){
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );

        dialog.show();
    }

  private void addWorkout(String title, String exercise, String amount, String reps ){
        if(title.isEmpty()){
            this.title.setError("Enter a Valid title");
            this.title.requestFocus();
        } else if (exercise.isEmpty()){
            this.exercise.setError("Enter an Exercise Name");
            this.exercise.requestFocus();
        } else if (amount.isEmpty() || amount.equals("0")){
            this.amount.setError("Enter an amount that is not 0");
            this.amount.requestFocus();
        } else if (reps.isEmpty() || reps.equals("0")){
            this.reps.setError("Enter a number of reps that is not 0");
            this.reps.requestFocus();
        } else {
            Workout workout = new Workout(title, exercise, Integer.parseInt(amount), Integer.parseInt(reps), time, name, new ArrayList<>());
            FunctionsInterface.addWorkout(workout, ClassFragment.getname1()).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Exception e = task.getException();
                    if(e instanceof FirebaseFunctionsException){
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                        Object details = ffe.getDetails();
                        Log.e("FUNCTIONS ERROR", e.getMessage().toString());
                    } else {

                      Snackbar.make(cLayout, task.getResult().toString(),Snackbar.LENGTH_SHORT ).show();

                    }
                }
            });
        }
  }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

            time = calendar.getTimeInMillis();



    }





}
