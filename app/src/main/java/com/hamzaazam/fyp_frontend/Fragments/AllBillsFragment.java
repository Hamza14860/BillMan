package com.hamzaazam.fyp_frontend.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.hamzaazam.fyp_frontend.HomeFragment;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.ViewStatsActivity;
import com.hamzaazam.fyp_frontend.ocrActivity;

import java.util.ArrayList;
import java.util.List;


public class AllBillsFragment extends Fragment {

    public final static String DATA_RECEIVE = "data_receive";
    String receivedCategoryName;

    RecyclerView recyclerViewBills;
    BillsAdapter billsAdapter;
    private List<BillM> billMList;
    FloatingActionButton addBillButton;
    FloatingActionButton viewStatsButton;

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
        viewStatsButton = view.findViewById(R.id.view_stats_button);

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
                    //TODO : bill text might have issue when receiving it
                    //to only show the clicked category bills
                    if(e.getBillCategory().equals(receivedCategoryName)){
                        e.setBillId(bill.getKey());
                        e.setUserID(fuserid);
                        billMList.add(e);
                        billsAdapter.notifyDataSetChanged();

                    }
                    pDialog.hide();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        billsAdapter=new BillsAdapter(getContext(),billMList);
        recyclerViewBills.setAdapter(billsAdapter);

        //ADD BILL FAB ONCLICK
        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ocrActivity.class);
                startActivity(intent);
            }

        });
        addBillButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                makeToast(addBillButton, "Extract Text from Bill Image");
                return true;
            }
        });


        //VIEW STATS FAB ONCLICK
        viewStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewStatsActivity.class);
                intent.putExtra("billcat",receivedCategoryName);
                startActivity(intent);
            }

        });
        viewStatsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                makeToast(viewStatsButton, "View All Bills Stats");
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

//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                if (keyCode == KeyEvent.KEYCODE_BACK)
//                {
//                    toast("Select Option From Navigation Drawer");
//                    //getActivity().getSupportFragmentManager().popBackStackImmediate() ;
//
////                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                            new HomeFragment()).commit();
//                    return true;
//                }
//                return false;
//            }
//        });
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
                    final BillM bill = snapshot.getValue(BillM.class);
                    assert billMList != null;

                    //to only show the clicked category bills
                    if(bill.getBillCategory().equals(receivedCategoryName)){
                        billMList.add(bill);
                    }

                }
                billsAdapter = new BillsAdapter(getContext(), billMList);
                recyclerViewBills.setAdapter(billsAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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


    //In this function we receive the type of category which was clicked by the user
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            receivedCategoryName=args.getString(DATA_RECEIVE);
            //toast( receivedCategoryName+ " Bills");
        }
        else{
            receivedCategoryName="Category Name was NULL";
            toast("Category Name was NULL");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                toast("Select Option From Navigation Drawer");
                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                //getChildFragmentManager().popBackStackImmediate();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
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
