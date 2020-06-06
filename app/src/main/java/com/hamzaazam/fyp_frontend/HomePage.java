package com.hamzaazam.fyp_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hamzaazam.fyp_frontend.Fragments.HelpFragment;
import com.hamzaazam.fyp_frontend.Fragments.ExpensesFragment;
import com.hamzaazam.fyp_frontend.Fragments.HomehFragment;
import com.hamzaazam.fyp_frontend.Fragments.ProfileFragment;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variable Decleration
    private DrawerLayout drawer;
    public Toolbar toolbar;
    ImageView NavprofileImage;
    TextView NavName, NavEmail;
    FirebaseAuth mAuth;
    NavigationView navigationView;
    Button profileSettings;

    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //avoid automatically appear android keyboard when activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Variable Assignments
        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();


        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        NavEmail = hView.findViewById(R.id.NavEmail);
        NavName = hView.findViewById(R.id.NavName);
        NavprofileImage = hView.findViewById(R.id.NavProfileImage);
        profileSettings = hView.findViewById(R.id.profieSettings);
        profileSettings.setVisibility(View.GONE);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);//to keep code clear we passed this, and implemented the method

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomehFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_homehome);
        }

        //Account settings button
//        profileSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
//            }
//        });


        //Calling Helper Methods
        //Loading User Profile On Nav-Header
        SetNavHeader();

    }

    //Helper Methods
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        final Runnable[] mPendingRunnable = {new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                switch (item.getItemId()) {
                    case R.id.nav_homehome:
                        getSupportActionBar().setTitle(" Home ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomehFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_home:
                        getSupportActionBar().setTitle(" Bill Management ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new BillmanFragment()).addToBackStack(null).commit();
                        break;
//                    case R.id.nav_players:
//                        getSupportActionBar().setTitle(" OCR Receipts ");
//
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                new OcrReceiptsFragment()).addToBackStack(null).commit();
//                        break;
                    case R.id.nav_profile:
                        getSupportActionBar().setTitle(" Profile ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_events:
                        getSupportActionBar().setTitle(" Expense Management ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ExpensesFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_help:
                        getSupportActionBar().setTitle(" Help ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HelpFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.nav_logout:

                        String current_id = mAuth.getCurrentUser().getUid();
                        ///token_id to null
                        Map<String, Object> tokenMapRemove = new HashMap<>();
                        tokenMapRemove.put("token_id", FieldValue.delete());

                        mFirestore.collection("Users").document(current_id).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                /////////
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomePage.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                Toasty.success(getApplicationContext(), "Logged Out!", Toast.LENGTH_LONG, true).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Failed to clear tokenid", e.getMessage());
                            }
                        });
                        //////
                        //Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();


                        break;
                    case R.id.nav_about:
                        toast("Devs: Hamza, Shariq, Anees");
                        break;
                }
            }
        }};
        drawer.closeDrawer(GravityCompat.START);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                if (mPendingRunnable[0] != null) {
                    Handler mHandler = new Handler();
                    mHandler.post(mPendingRunnable[0]);
                    mPendingRunnable[0] = null;
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        return true;
    }


    //Over Write Functions

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!drawer.isDrawerOpen(GravityCompat.START) && navigationView.getCheckedItem().getItemId() != R.id.nav_home ){
            getSupportActionBar().setTitle(" Home ");
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BillmanFragment()).commit();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    //(Sansi) Setting up Nav Header From Firebase
    public void SetNavHeader(){
        FirebaseUser user = mAuth.getCurrentUser();

        if(user.getPhotoUrl() == null || user.getPhotoUrl().toString().equals("None Chosen")){
            Glide.with(this)
                    .load(R.mipmap.contact_photo_def)
                    .into(NavprofileImage);

        }
        else{
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(NavprofileImage);
        }


        if(user.getDisplayName() != null) {
            NavName.setText(user.getDisplayName());
        }

        if(user.getEmail() != null) {
            NavEmail.setText(user.getEmail());
        }
    }


    private void toast(String msg){
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG).show();
    }



}
