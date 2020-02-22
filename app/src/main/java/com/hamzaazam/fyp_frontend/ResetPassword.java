package com.hamzaazam.fyp_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPassword extends AppCompatActivity {

    Button btncon;
    FirebaseAuth mAuth;
    ProgressBar verifypreogress;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        verifypreogress = findViewById(R.id.verifyprogress);
        btncon = findViewById(R.id.btnContinue);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.resetEmail);


        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText= email.getText().toString();
                if(emailText.isEmpty()) {
                    email.setError("Email Not entered");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    email.setError("Invalid Email Format");
                    email.requestFocus();
                    return;
                }
                verifypreogress.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(emailText)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    verifypreogress.setVisibility(View.GONE);
                                    toast("Password reset link is sent");
                                    toast("Password reset link sent");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else {
                                    verifypreogress.setVisibility(View.GONE);
                                    toast(task.getException().getMessage());
                                }
                            }
                        });

            }
        });
    }



    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();
    }
}

