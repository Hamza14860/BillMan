package com.hamzaazam.fyp_frontend.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.hamzaazam.fyp_frontend.Adapter.BillsAdapter;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

import java.util.ArrayList;
import java.util.List;


public class AllBillsFragment extends Fragment {

    RecyclerView recyclerViewBills;
    BillsAdapter billsAdapter;
    private List<BillM> billMList;
    FloatingActionButton addBillButton;
    DatabaseReference reference;
    EditText search_bar;

    ProgressDialog pDialog;

    String fuserid;//current user logged in

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_bills, container, false);

        ////////////

        addBillButton = view.findViewById(R.id.add_bill_button);
        search_bar = view.findViewById(R.id.searchBills);

        recyclerViewBills = view.findViewById(R.id.recyclerViewBills);
        recyclerViewBills.setHasFixedSize(true);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getContext()));

        billMList=new ArrayList<>();


        //////////////
        fuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Populating Category list
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading Bills");
        pDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billMList.clear();
                for(DataSnapshot bill : dataSnapshot.getChildren()){
                    BillM e = bill.getValue(BillM.class);
                    e.setBillId(bill.getKey());
                    e.setUserID(fuserid);
                    billMList.add(e);
                    billsAdapter.notifyDataSetChanged();
                    pDialog.hide();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        billsAdapter=new BillsAdapter(getContext(),billMList);
        recyclerViewBills.setAdapter(billsAdapter);


        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), CreateCategoryActivity.class);
//                startActivity(intent);


//                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
//                CreateCategoryFragment ccf=new CreateCategoryFragment();
//                ft.replace(R.id.child_fragment_container,ccf);
//                //ft.addToBackStack(null);
//                ft.commit();



            }

        });
        addBillButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
//                toast("Create Category");
                makeToast(addBillButton, "Create Category");
                return true;
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchBills(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        recyclerViewBills.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){

                    // Do what you want
                    addBillButton.hide();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    // Do what you want
                    addBillButton.show();
                }
                v.onTouchEvent(event);
                return true;
            }
        });


        ///////////
        return view;
    }


    private void searchBills(String s){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Query query= FirebaseDatabase.getInstance().getReference("bills").child(firebaseUser.getUid()).orderByChild("billDate")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billMList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final BillM category = snapshot.getValue(BillM.class);
                    assert billMList != null;
                    billMList.add(category);
                }
                billsAdapter = new BillsAdapter(getContext(), billMList);
                recyclerViewBills.setAdapter(billsAdapter);
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
