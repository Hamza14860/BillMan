package com.hamzaazam.fyp_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Button btnLogin;
    TextView tvSignup, forgetPassword;
    EditText userEmail, userPassword;
    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=findViewById(R.id.btnLogin);
        tvSignup=findViewById(R.id.registerM);
        userEmail = findViewById(R.id.loginemail);
        userPassword = findViewById(R.id.loginpass);
        forgetPassword = findViewById(R.id.forgetPassword);
        progressBar = findViewById(R.id.signinprogress);

        mAuth = FirebaseAuth.getInstance();
        mFirestore= FirebaseFirestore.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Helper Function for user sign in
                LoginUser();
            }
        });


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset password Activity
                startActivity(new Intent(MainActivity.this, ResetPassword.class));
            }
        });
    }

    //if users is already logged in
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    //handels user login
    private void LoginUser(){
        String email, password;
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        if(email.isEmpty()){
            userEmail.setError("Can't leave email empty!");
            userEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please enter a valid email!");
            userEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            userPassword.setError("Can't leave password empty!");
            userPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            userPassword.setError("Minimum length must be greater or equal to 6!");
            userPassword.requestFocus();
            return;
        }

        //User Sign in via email and password
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //Checks for Email Verification
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        //if sign in successful, -> go to Home Activity
                        //toast("sign in successful!");

                        /////////Token for notification


                        String token_id= FirebaseInstanceId.getInstance().getToken();
                        String current_id=mAuth.getCurrentUser().getUid();

                        Map<String,Object> tokenMap=new HashMap<>();
                        tokenMap.put("token_id",token_id);
                        mFirestore.collection("Users").document(current_id).update( tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ////////
                                Toasty.success(getApplicationContext(), "Sign In Successful!", Toast.LENGTH_LONG, true).show();


                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });




                    } else {
                        // Not Verified, -> go to VerifyEmail Activity
                        Intent intent = new Intent(MainActivity.this, VerifyEmail.class);
                        startActivity(intent);
                    }

                } else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    //if sign in failed
                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            toast("The custom token format is incorrect. Please check the documentation.");
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(MainActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(MainActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(MainActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                            userEmail.setError("The email address is badly formatted.");
                            userEmail.requestFocus();
                            break;
                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(MainActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                            userPassword.requestFocus();
                            userPassword.setText("");
                            userPassword.setError("password is incorrect ");
                            ResetPassword();
                            break;
                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(MainActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                            break;
                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(MainActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;
                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(MainActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            break;
                        case "ERROR_INVALID_USER_TOKEN":
                            Toast.makeText(MainActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
        return;
    }

    private void ResetPassword(){
        forgetPassword.setTextColor(Color.RED);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset password Activity
                startActivity(new Intent(MainActivity.this, ResetPassword.class));
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();
    }


}


