package com.htc.fitnesspartner;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public interface FunctionsInterface {

    static Task addAthleteToClass(String uid, String classId){
        Map<String, String> data = new HashMap<>();
        data.put("uid", uid);
        data.put("classId", classId);


        return FirebaseFunctions.getInstance().getHttpsCallable("addAthleteToClass").call(data).continueWith(task ->
        {

                   String res = (String) task.getResult().getData();
                   Log.e("err",res );
                 return res;
               });
    }

    static Task deleteAthlete(String uid, String classId){
        Map<String, String> data = new HashMap<>();
        data.put("uid", uid);
        data.put("classId", classId);

        return FirebaseFunctions.getInstance().getHttpsCallable("undoAddAthlete").call(data).continueWith(task -> (String) task.getResult().getData());
    }


    static Task addWorkout(Workout workout, String name ){
        Map<String, Object> data = new HashMap<>();
        data.put("name", workout.getName());
        data.put("exercise", workout.getExercise());
        data.put("amount", workout.getAmount());
        data.put("reps", workout.getReps());
        data.put("date", workout.getDate());
        data.put("className", name);
        data.put("parentClass", workout.getParentClass());

        return FirebaseFunctions.getInstance().getHttpsCallable("addWorkout").call(data).continueWith(task -> (String) task.getResult().getData());

    }

//    static Task completeWorkout() {
//
//    }

}
