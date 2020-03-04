package com.hamzaazam.fyp_frontend.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.hamzaazam.fyp_frontend.HomePage;
import com.hamzaazam.fyp_frontend.MainActivity;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.UserInfo;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    CircleImageView profileImage;
    TextView profileUsername;
    TextView profileEmail;

    FirebaseUser fuser;
    DatabaseReference reference;



    Button btnLogout;
    Button btnDeleteAcc;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);


        mFirestore=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        profileImage=view.findViewById(R.id.profileImage);
        profileUsername=view.findViewById(R.id.tvUsername);
        profileEmail=view.findViewById(R.id.tvEmail);


        btnDeleteAcc=view.findViewById(R.id.btnDelAccount);
        btnLogout=view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_id = mAuth.getCurrentUser().getUid();
                ///token_id to null
                Map<String, Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());

                mFirestore.collection("Users").document(current_id).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /////////
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toasty.success(getContext(), "Logged Out!", Toast.LENGTH_LONG, true).show();
                        getActivity().finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Failed to clear tokenid", e.getMessage());
                    }
                });
            }
        });

        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("DISABLED");
            }
        });

        /////////GETTING PROFILE INFO

        fuser= FirebaseAuth.getInstance().getCurrentUser();

        if(fuser.getPhotoUrl().toString().equals("None Chosen") || fuser.getPhotoUrl().toString().equals("default")){
            Glide.with(this)
                    .load(R.mipmap.contact_photo_def)
                    .into(profileImage);
        }
        else if(fuser.getPhotoUrl() != null){

            Glide.with(this)
                    .load(fuser.getPhotoUrl())
                    .into(profileImage);

        }

        if(fuser.getDisplayName() != null) {
            profileUsername.setText(fuser.getDisplayName());
        }

        if(fuser.getEmail() != null) {
            profileEmail.setText(fuser.getEmail());
        }


        return view;
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();
    }

}
