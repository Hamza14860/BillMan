package com.hamzaazam.fyp_frontend.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.Model.ReceiptM;
import com.hamzaazam.fyp_frontend.R;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class ReceiptTextFragment extends BillTextFragment {


    public static final String RECID_RECEIVE = "nil";
    private TextView recTitle;
    private EditText recAmount;
    private EditText recDate;
    private EditText recName;
    private EditText recCategory;
    private EditText recDesc;



    private String receivedRecId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rec_text, container, false);

        vieww=view;

        cvMno_Units=view.findViewById(R.id.cvP5);
        cvPno=view.findViewById(R.id.cvP6);

        recTitle=view.findViewById(R.id.tvBillTitle);
        recDesc=view.findViewById(R.id.billAddNote);
        recAmount=view.findViewById(R.id.billAmount);
        recCategory=view.findViewById(R.id.billCustomerAddress);
        recName=view.findViewById(R.id.billCustomerName);
        recDate=view.findViewById(R.id.billDate);


        btnSaveData=view.findViewById(R.id.btnSaveData);


        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });



        return view;

    }

    @Override
    void populateDataOnScreen() {
        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        Log.w("fuserid", fuserid.toString());

        Log.w("recID", receivedRecId.toString()+ " rec ids");

        reference = FirebaseDatabase.getInstance().getReference("GenericReceipt").child(fuserid).child(receivedRecId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ReceiptM rec = dataSnapshot.getValue(ReceiptM.class);

//                try {
//
//                }

//                BillM bill = dataSnapshot.getValue(BillM.class);
//
//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//                    tvBillTitle.setText(bill.getBillCategory());
//                }
//                if(!(bill.getBillAmount().equals("-")) && bill.getBillAmount()!=null) {
//                    billAmount.setText(bill.getBillAmount());
//                }
//                if(!(bill.getBillDate().equals("-")) && bill.getBillDate()!=null) {
//                    billDate.setText(bill.getBillDate());
//                }
//                if(!(bill.getBillCustomerName().equals("-")) && bill.getBillCustomerName()!=null) {
//                    billCustomerName.setText(bill.getBillCustomerName());
//                }
//                if(!(bill.getBillAddNote().equals("-")) && bill.getBillAddNote()!=null) {
//                    billAddNote.setText(bill.getBillAddNote());
//                }
//                if (bill.getBillCategory().equals("PTCL")){//hide units and meter no if category is ptcl
//                    cvMno_Units.setVisibility(View.GONE);
//                }
//                else{//Hide phone no card view if bill category is either iesco or sui gas
//                    cvPno.setVisibility(View.GONE);
//                }
//




//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//                    tvBillTitle.setText(bill.getBillCategory());
//                }
//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//                    tvBillTitle.setText(bill.getBillCategory());
//                }
//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//                    tvBillTitle.setText(bill.getBillCategory());
//                }
//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//                    tvBillTitle.setText(bill.getBillCategory());
//                }
                /////////
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ////////////////////////
    }

    //In this function we receive the bill id which was clicked by the user
    @Override
    public void onStart() {

        Bundle args = getArguments();
        if (args != null) {
            receivedRecId = args.getString(RECID_RECEIVE);
                toast( receivedRecId+ " BillID");
        }
        else{
            receivedRecId="nil";
            toast("REC ID was NULL");
        }

        super.onStart();

        populateDataOnScreen();




    }

    public void updateInfo(){

//        fuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedRecId);
//        HashMap<String ,Object> map2 =new HashMap<>();
//        if(billAddNote.getText()!=null) {
//            map2.put("billAddNote", billAddNote.getText().toString());
//        }
//        if( (billAmount.getText()!=null) && !(billAmount.getText().equals("")) ){
//            map2.put("billAmount",billAmount.getText().toString());
//        }
//
//        if( (billCustomerName.getText()!=null) && !(billCustomerName.getText().equals("")) ){
//            map2.put("billCustomerName",billCustomerName.getText().toString());
//        }
//        if( (billDate.getText()!=null) && !(billDate.getText().equals("")) ){
//            map2.put("billDate",billDate.getText().toString());
//        }
//        if(tvBillTitle.getText().toString().equals("PTCL") || tvBillTitle.getText().toString().contains("Pakistan Telecommunication") )
//        {
//            //TODO
//            //Update other fields in billText Node
//        }
//        else if(tvBillTitle.getText().toString().equals("IESCO") || tvBillTitle.getText().toString().contains("ISLAMABAD") )
//        {
//            //TODO
//            //Update other fields in billText Node
//        }
//        else if(tvBillTitle.getText().toString().equals("SUI GAS") || tvBillTitle.getText().toString().contains("GAS") )
//        {
//            //TODO
//            //Update other fields in billText Node
//        }
//
//        reference2.updateChildren(map2);
//
//
//        Toasty.success(getContext(), "Updated Successfully!", Toast.LENGTH_LONG, true).show();
//
//    }

}}


