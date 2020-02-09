package com.hamzaazam.fyp_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {

    TextView resend, resendlabel;
    Button btncon;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar verifypreogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        verifypreogress = findViewById(R.id.verifyprogress);
        btncon = findViewById(R.id.btnContinue);
        resendlabel = findViewById(R.id.reSendLabel);
        resend = findViewById(R.id.reSend);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifypreogress.setVisibility(View.VISIBLE);
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(user.isEmailVerified()){
                            verifypreogress.setVisibility(View.GONE);
                            toast("Email Verified");
                            finish();
                            startActivity(new Intent(VerifyEmail.this, HomePage.class));
                        }else {
                            verifypreogress.setVisibility(View.GONE);
                            resendlabel.setText("Didn't receive a verification link? ");
                            resend.setText("Resend.");

                        }

                    }
                });
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending verification link
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toast("Verification Email Sent");
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
