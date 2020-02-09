package com.hamzaazam.fyp_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 701;
    //Defined Variables
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button btnRegister;
    EditText userName, userEmail, userPassword, tempPassword;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    Uri uriProfile;
    public String ProfileImageURL;

    FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Register ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Variable Assignments
        btnRegister = findViewById(R.id.btnRegister);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.pass);
        tempPassword = findViewById(R.id.passre);
        progressBar = findViewById(R.id.registerationprogress);
        profileImage = findViewById(R.id.pImage);
        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();




        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //function to select user profile image
                ProfileSelect();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Helper Method used to Register a new user
                RegisterUser();
            }
        });

    }

    //Helper Methods



    //Registers A New User
    private boolean RegisterUser(){
        String name, email, password, repassword ;
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        repassword= tempPassword.getText().toString();

        if(name.isEmpty()){
            userName.setError("Can't leave name empty!");
            userName.requestFocus();
            return false;
        }

        if(email.isEmpty()){
            userEmail.setError("Can't leave email empty!");
            userEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please enter a valid email!");
            userEmail.requestFocus();
            return false;
        }

        if(password.isEmpty()){
            userPassword.setError("Can't leave password empty!");
            userPassword.requestFocus();
            return false;
        }
        if(password.length() < 6){
            userPassword.setError("Minimum length must be greater or equal to 6!");
            userPassword.requestFocus();
            return false;
        }
        if(!(password.equals(repassword))){
            tempPassword.setError("Password doesn't match!");
            tempPassword.requestFocus();
            return false;
        }

        //Authenticating User via Email and Password
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Saving User Information to Firebase
                    UpLoadImage();
                    progressBar.setVisibility(View.GONE);
                    //Registeration in successful, -> go to Home Activity
                    toast("User Registered!");
                    Intent intent=new Intent(RegisterActivity.this,VerifyEmail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        progressBar.setVisibility(View.GONE);
                        userEmail.setError("Email Already Registered! ");
                        userEmail.requestFocus();

                    }
                    toast(task.getException().getMessage());
                }
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData() != null){

            uriProfile = data.getData();
            profileImage.setImageURI(uriProfile);
        }
    }

    //Used to launch an intent to select an image from the system
    private void ProfileSelect(){
        Intent imgchooser = new Intent();
        imgchooser.setType("image/*");
        imgchooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgchooser, "Select Profile Image"), CHOOSE_IMAGE);
    }

    //Uploads image to firebase storage
    private void UpLoadImage(){
        String uniqueId = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance()
                .getReference("ProfilePictures/"+uniqueId+".jpg");

        //add file on Firebase and got Download Link
        if(uriProfile != null){
            reference.putFile(uriProfile).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        ProfileImageURL = task.getResult().toString();
//                    System.out.println ("$$$$$$$$$$$\n"+task.getResult().toString());

                        //Because UploadImage() method was executing after SaveUserInformation() method
                        SaveUserInformation();



                    }else {
//                    System.out.println ("$$$$$$$$$$$\n"+task.getResult().toString());
                        toast("Something went wrong, Image not Uploaded");
                    }
                }
            });
        }else {
            ProfileImageURL = "None Chosen";
            SaveUserInformation();
        }

    }


    // Saves User Information to Firebase
    private void SaveUserInformation() {
        final String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        //sending verification link
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    toast("Verification Email Sent");
                }
            }
        });

        //Setting User Attributes
        UserInfo userInfo = new UserInfo(name, email, ProfileImageURL);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid())
                .setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toast("User Attributes Added!");
                } else {
                    toast("Error Registering User Attribute!");
                }
            }
        });
//        //Hamza Bhai
//        PlayerM playerReg=new PlayerM(user.getUid(), name, email ,ProfileImageURL);
//        FirebaseDatabase.getInstance().getReference("Players")
//                .child(user.getUid())
//                .setValue(playerReg).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.i("Player Attr Added",name);
//                } else {
//                    toast("Error Registering Player Attribute!");
//                }
//            }
//        });

        String token_id= FirebaseInstanceId.getInstance().getToken();

        Map<String,Object> userMap=new HashMap<>();
        userMap.put("Name",name);
        userMap.put("ProfileURL",ProfileImageURL);
        userMap.put("token_id",token_id);
        mFirestore.collection("Users").document(user.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Attr Added in Firestore",name);

            }
        });

        //Updating User Profile
        UserProfileChangeRequest profileUpdates;
        if(ProfileImageURL != null){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(ProfileImageURL))
                    .build();
        }else {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
        }
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("user profile updated!");
                        }else{
                            toast("updating user profile failed!");
                        }
                    }
                });
    }

    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();
    }
}
