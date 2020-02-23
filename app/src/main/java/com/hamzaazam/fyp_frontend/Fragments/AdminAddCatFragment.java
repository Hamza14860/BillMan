package com.hamzaazam.fyp_frontend.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hamzaazam.fyp_frontend.Model.CategoryM;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.RegisterActivity;
import com.hamzaazam.fyp_frontend.VerifyEmail;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AdminAddCatFragment extends Fragment {
    private static final int CHOOSE_IMAGE = 701;

    ImageView catImage;

    Button btnAddCat;
    EditText catName;


    Uri uriProfile;
    public String catImageURL;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin_add_cat, container, false);

        catImage = view.findViewById(R.id.catImage);
        catName= view.findViewById(R.id.catName);
        btnAddCat=view.findViewById(R.id.btnAddCat);


        mAuth = FirebaseAuth.getInstance();

        catImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //function to select user profile image
                ImageSelect();

            }
        });

        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Helper Method used to Register a new user
                AddCategory();
            }
        });
        return view;
    }


    //Registers A New User
    private boolean AddCategory(){
        String nameC;
        nameC = catName.getText().toString();

        if(nameC.isEmpty()){
            catName.setError("Can't leave name empty!");
            catName.requestFocus();
            return false;
        }

        ////////
        UpLoadImage();


        return true;
    }

    //Used to launch an intent to select an image from the system
    private void ImageSelect(){
        Intent imgchooser = new Intent();
        imgchooser.setType("image/*");
        imgchooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgchooser, "Select Category Image"), CHOOSE_IMAGE);
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData() != null){

            uriProfile = data.getData();
            catImage.setImageURI(uriProfile);
        }
    }

    //Uploads image to firebase storage
    private void UpLoadImage(){
        String uniqueId = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance()
                .getReference("CategoryPictures/"+uniqueId+".jpg");

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
                        catImageURL = task.getResult().toString();
                        //Because UploadImage() method was executing after SaveUserInformation() method
                        SaveCatInformation();



                    }else {
                        toast("Something went wrong, Image not Uploaded");
                    }
                }
            });
        }else {
            catImageURL = "None Chosen";
            SaveCatInformation();
        }

    }


    // Saves Category Information to Firebase
    private void SaveCatInformation() {
        final String name = catName.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();


        //Setting User Attributes
        CategoryM catInfo = new CategoryM(name, catImageURL,user.getUid());

        FirebaseDatabase.getInstance().getReference("Categories")
                .push()
                .setValue(catInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    toast("Category Added!");
                } else {
                    toast("Error Registering User Attribute!");
                }
            }
        });

    }


    private void toast(String msg){
        Toast.makeText(getView().getContext(), msg,
                Toast.LENGTH_LONG).show();
    }


}
