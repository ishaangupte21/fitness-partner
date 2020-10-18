package com.htc.fitnesspartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText, firstNameEditText, lastNameEditText;
    private Button signUp;
    private CheckBox checkbox;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean coach = false;
    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.yesAcct).setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, MainActivity.class)));

        emailEditText = findViewById(R.id.signUpEmail);
        passwordEditText = findViewById(R.id.signUpPassword);
        firstNameEditText = findViewById(R.id.signUpFN);
        lastNameEditText = findViewById(R.id.signUpLN);

        mFunctions = FirebaseFunctions.getInstance();


        checkbox = findViewById(R.id.coachCheck);

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> coach = isChecked);

        signUp = findViewById(R.id.signUpBtn);

        signUp.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString().toLowerCase().trim();
            final String password = passwordEditText.getText().toString().trim();
            final String fn = firstNameEditText.getText().toString().trim();
            final String ln = lastNameEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("Enter a valid Email");
                emailEditText.requestFocus();
            }

            if (password.isEmpty() || password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters");
                passwordEditText.requestFocus();
            }

            if (fn.isEmpty()) {
                firstNameEditText.setError("Enter a First Name");
                firstNameEditText.requestFocus();
            }

            if (ln.isEmpty()) {
                lastNameEditText.setError("Enter a Last Name");
                lastNameEditText.requestFocus();
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                Toast.makeText(getApplicationContext(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();

                FirebaseUser user = authResult.getUser();
                AdditionalUserInfo i = authResult.getAdditionalUserInfo();

                //Switch it to backend after it starts working again

                if (coach) {
                    List<String> classes = new ArrayList<>();

                    Coach coach = new Coach(fn, ln, email, classes, System.currentTimeMillis());

                    createCoachNode(coach).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();

                                Log.e("Functions Exception", details.toString());
                            }
                        } else {
                            startActivity(new Intent(SignUpActivity.this, CoachActivity.class));
                            finish();
                        }
                    });

                } else {
                    List<String> enrolledClasses = new ArrayList<>();
                    Athlete athlete = new Athlete(fn, ln, email, System.currentTimeMillis(), enrolledClasses, new ArrayList<>(), 0);

                    createAthleteNode(athlete).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                                Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();

                                Log.e("Functions Exception", details.toString());
                            }
                        } else {
                            startActivity(new Intent(SignUpActivity.this, AthleteActivity.class));
                            finish();
                        }
                    });
                }

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

        });

    }

    private Task<Coach> createCoachNode(Coach coach) {
        Map<String, Object> data = new HashMap<>();
        data.put("fn", coach.getFirstName());
        data.put("ln", coach.getLastName());
        data.put("email", coach.getEmailAddress());
        data.put("dateJoined", coach.getDateJoined());

        return mFunctions.getHttpsCallable("createCoachNode").call(data)
                .continueWith(task -> null);
    }


    private Task<Athlete> createAthleteNode(Athlete athlete) {
        Map<String, Object> data = new HashMap<>();
        data.put("fn", athlete.getFirstName());
        data.put("ln", athlete.getLastName());
        data.put("email", athlete.getEmailAddress());
        data.put("dateJoined", athlete.getDateJoined());

        return mFunctions.getHttpsCallable("createAthleteNode").call(data)
                .continueWith(task -> null);
    }

}
