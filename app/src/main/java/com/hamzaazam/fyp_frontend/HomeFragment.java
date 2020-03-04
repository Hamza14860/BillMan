package com.hamzaazam.fyp_frontend;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Adapter.CategoryAdapter;
import com.hamzaazam.fyp_frontend.Fragments.CreateCategoryFragment;
import com.hamzaazam.fyp_frontend.Model.CategoryM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    //OCR BILL CATEOGIRES
    RecyclerView recyclerViewCategoires;
    CategoryAdapter categoryAdapter;
    private List<CategoryM> categoryMList;
    FloatingActionButton createCategoryButton;
    DatabaseReference reference;
    EditText search_bar;

    ProgressDialog pDialog;


    Fragment currFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        createCategoryButton = view.findViewById(R.id.create_category_button);
        search_bar = view.findViewById(R.id.searchCategories);

        recyclerViewCategoires = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategoires.setHasFixedSize(true);
        recyclerViewCategoires.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryMList=new ArrayList<>();


        //////////////


        //Populating Category list
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading Categories");
        pDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryMList.clear();
                for(DataSnapshot category : dataSnapshot.getChildren()){
                    CategoryM e = category.getValue(CategoryM.class);
                    e.setCatId(category.getKey());
                    //e.setCatUrl("None Chosen");
                    categoryMList.add(e);
                    categoryAdapter.notifyDataSetChanged();
                    pDialog.hide();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

       currFrag=getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        categoryAdapter=new CategoryAdapter(getContext(),categoryMList,currFrag);
        recyclerViewCategoires.setAdapter(categoryAdapter);


        createCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), CreateCategoryActivity.class);
//                startActivity(intent);


                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
                CreateCategoryFragment ccf=new CreateCategoryFragment();
                ft.replace(R.id.child_fragment_container,ccf);
                //ft.addToBackStack(null);
                ft.commit();



            }

        });
        createCategoryButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
//                toast("Create Category");
                makeToast(createCategoryButton, "Create Category");
                return true;
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchCategories(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        recyclerViewCategoires.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    // Do what you want
                    createCategoryButton.hide();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    // Do what you want
                    createCategoryButton.show();
                }
                v.onTouchEvent(event);
                return true;
            }
        });


        /////////////
        return view;
    }




    private void searchCategories(String s){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Query query= FirebaseDatabase.getInstance().getReference("Categories").orderByChild("catName")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryMList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final CategoryM category = snapshot.getValue(CategoryM.class);
                    assert categoryMList != null;
                    categoryMList.add(category);
                }
                categoryAdapter = new CategoryAdapter(getContext(), categoryMList,currFrag);
                recyclerViewCategoires.setAdapter(categoryAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    public void makeToast(View view, String msg){

        int x = view.getLeft()+50;
        int y = view.getTop();
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.LEFT, x, y);
        toast.show();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();

        pDialog.dismiss();
    }
}

