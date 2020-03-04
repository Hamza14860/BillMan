package com.hamzaazam.fyp_frontend.Fragments;


import android.app.ProgressDialog;
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
import android.util.Log;
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
import com.hamzaazam.fyp_frontend.Adapter.ReceiptsAdapter;
import com.hamzaazam.fyp_frontend.Model.CategoryM;
import com.hamzaazam.fyp_frontend.Model.ReceiptM;
import com.hamzaazam.fyp_frontend.R;

import java.util.ArrayList;
import java.util.List;

public class OcrReceiptsFragment extends Fragment {

    //OCR BILL CATEOGIRES
    RecyclerView recyclerViewRecs;
    ReceiptsAdapter receiptsAdapter;
    private List<ReceiptM> receiptMList;

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

        recyclerViewRecs = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewRecs.setHasFixedSize(true);
        recyclerViewRecs.setLayoutManager(new LinearLayoutManager(getContext()));

        receiptMList = new ArrayList<>();


        //////////////


        //Populating Category list
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading Receipts");
        pDialog.show();


        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference("GenericReceipt").child(currentUser);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiptMList.clear();
                long childrenCount = dataSnapshot.getChildrenCount();
                Log.e("INFOINFO222", String.valueOf(childrenCount));
                for(DataSnapshot rec : dataSnapshot.getChildren()){
                    Log.e("INFOINFO" , rec.toString());
                    ReceiptM e = rec.getValue(ReceiptM.class);

                    Log.e("ERROR", e.toString());
                   // e.setRecCategory(rec.getKey());

                    //e.setCatUrl("None Chosen");
                    //receiptMList.add(e);
                    receiptsAdapter.notifyDataSetChanged();
                    pDialog.hide();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.toString());

            }
        });

        currFrag=getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        receiptsAdapter=new ReceiptsAdapter(getContext(),receiptMList);

        recyclerViewRecs.setAdapter(receiptsAdapter);


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



        recyclerViewRecs.setOnTouchListener(new View.OnTouchListener() {

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
                receiptMList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final ReceiptM category = snapshot.getValue(ReceiptM.class);
                    assert receiptMList != null;
                    receiptMList.add(category);
                }
                receiptsAdapter = new ReceiptsAdapter(getContext(), receiptMList);
                recyclerViewRecs.setAdapter(receiptsAdapter);
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

    private void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();
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

