package com.hamzaazam.fyp_frontend.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

import java.util.ArrayList;

public class AllReceiptFragment extends AllBillsFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_bills, container, false);

        ////////////

        addBillButton = view.findViewById(R.id.add_bill_button);
        viewStatsButton = view.findViewById(R.id.view_stats_button);

        search_bar = view.findViewById(R.id.searchBills);

        recyclerViewBills = view.findViewById(R.id.recyclerViewBills);
        recyclerViewBills.setHasFixedSize(true);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getContext()));

        billMList = new ArrayList<>();


        //////////////
        fuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Populating Category list
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading Bills");
        pDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billMList.clear();
                for (DataSnapshot bill : dataSnapshot.getChildren()) {
                    BillM e = bill.getValue(BillM.class);
                    //TODO : bill text might have issue when receiving it
                    //to only show the clicked category bills
                    if (e.getBillCategory().equals(receivedCategoryName)) {
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

        return view;
    }

}

