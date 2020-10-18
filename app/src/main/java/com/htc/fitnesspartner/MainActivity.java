package com.htc.fitnesspartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private Button signIn;
    private SignInButton mSignInBtn;
    private GoogleApiClient mClient;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mAuth.signOut();
        emailEditText = (TextInputEditText) findViewById(R.id.loginEmail);
        passwordEditText = (TextInputEditText) findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
        signIn = (Button) findViewById(R.id.signinBtn);

        mSignInBtn = findViewById(R.id.google_sign_in_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signIn.setOnClickListener(v -> {
                    String email = emailEditText.getText().toString().toLowerCase().trim();
                    String password = passwordEditText.getText().toString().trim();
                    Log.d("TAG", password);

                    if (email.isEmpty() || email.equals(null)) {
                        emailEditText.setError("Enter an Email");
                        emailEditText.requestFocus();
                    } else if (password.isEmpty() || password.equals(null)) {
                        passwordEditText.setError("Enter a Password");
                        passwordEditText.requestFocus();
                    } else {

                        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                            FirebaseUser user = authResult.getUser();

                            Toast.makeText(getApplicationContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();


                            user.getIdToken(false).addOnSuccessListener(getTokenResult -> {

                                boolean isCoach = (boolean) getTokenResult.getClaims().get("coach");

                                if (isCoach) {
                                    startActivity(new Intent(MainActivity.this, CoachActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(MainActivity.this, AthleteActivity.class));
                                    finish();
                                }

                            });

                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show());


                    }
                });



        findViewById(R.id.noAcct).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));


        initUI(mAuth.getCurrentUser());

    }

    private void initUI(FirebaseUser user){
        if(user != null){
            user.getIdToken(false).addOnSuccessListener(getTokenResult -> {
                Log.v("id token", String.valueOf(getTokenResult.getClaims().get("coach")));
                boolean isCoach = (boolean) getTokenResult.getClaims().get("coach");

                if(isCoach){
                    startActivity(new Intent(MainActivity.this, CoachActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this, AthleteActivity.class));
                    finish();
                }

            });
        } else{
            Log.v("USER", "no user") ;
            Toast.makeText(getApplicationContext(), "No User", Toast.LENGTH_SHORT).show();
        }
    }
}
