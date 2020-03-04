package com.hamzaazam.fyp_frontend.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillTextFragment extends Fragment {

    public final static String BILLID_RECEIVE = "data_receive";
    private String receivedBillId;

    View vieww;
    ////Setting & getting profile data
    DatabaseReference reference;
    String fuserid;

    CardView cvMno_Units;
    CardView cvPno;


    ///

    private TextView tvBillTitle;
    private EditText billAmount;
    private EditText billDate;
    private EditText billCustomerName;
    private EditText billCustomerAddress;
    private EditText billAddNote;
    private EditText billMeterNo;
    private EditText billUnits;
    ///

    Button btnSaveData;


    public BillTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bill_text, container, false);

        vieww=view;

        cvMno_Units=view.findViewById(R.id.cvP5);
        cvPno=view.findViewById(R.id.cvP6);

        tvBillTitle=view.findViewById(R.id.tvBillTitle);
        billAddNote=view.findViewById(R.id.billAddNote);
        billAmount=view.findViewById(R.id.billAmount);
        billCustomerAddress=view.findViewById(R.id.billCustomerAddress);
        billCustomerName=view.findViewById(R.id.billCustomerName);
        billDate=view.findViewById(R.id.billDate);
        billMeterNo=view.findViewById(R.id.billMeterNo);
        billUnits=view.findViewById(R.id.billUnits);

        btnSaveData=view.findViewById(R.id.btnSaveData);


        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });



        return view;

    }


    void populateDataOnScreen() {
        ////////////////////////
        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedBillId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BillM bill = dataSnapshot.getValue(BillM.class);

                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
                    tvBillTitle.setText(bill.getBillCategory());
                }
                if(!(bill.getBillAmount().equals("-")) && bill.getBillAmount()!=null) {
                    billAmount.setText(bill.getBillAmount());
                }
                if(!(bill.getBillDate().equals("-")) && bill.getBillDate()!=null) {
                    billDate.setText(bill.getBillDate());
                }
                if(!(bill.getBillCustomerName().equals("-")) && bill.getBillCustomerName()!=null) {
                    billCustomerName.setText(bill.getBillCustomerName());
                }
                if(!(bill.getBillAddNote().equals("-")) && bill.getBillAddNote()!=null) {
                    billAddNote.setText(bill.getBillAddNote());
                }
                if (bill.getBillCategory().equals("PTCL")){//hide units and meter no if category is ptcl
                    cvMno_Units.setVisibility(View.GONE);
                }
                else{//Hide phone no card view if bill category is either iesco or sui gas
                    cvPno.setVisibility(View.GONE);
                }
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
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            receivedBillId=args.getString(BILLID_RECEIVE);
            //toast( receivedBillId+ " BillID");
        }
        else{
            receivedBillId="BILL ID was NULL";

            toast("Bill ID was NULL");
        }


        //This function popluates the data on screen after grabbing it from Firebase
        populateDataOnScreen();




    }


    public void updateInfo(){

        fuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedBillId);
        HashMap<String ,Object> map2 =new HashMap<>();
        if(billAddNote.getText()!=null) {
            map2.put("billAddNote", billAddNote.getText().toString());
        }
        if( (billAmount.getText()!=null) && !(billAmount.getText().equals("")) ){
            map2.put("billAmount",billAmount.getText().toString());
        }

        if( (billCustomerName.getText()!=null) && !(billCustomerName.getText().equals("")) ){
            map2.put("billCustomerName",billCustomerName.getText().toString());
        }
        if( (billDate.getText()!=null) && !(billDate.getText().equals("")) ){
            map2.put("billDate",billDate.getText().toString());
        }
        if(tvBillTitle.getText().toString().equals("PTCL") || tvBillTitle.getText().toString().contains("Pakistan Telecommunication") )
        {
            //TODO
            //Update other fields in billText Node
        }
        else if(tvBillTitle.getText().toString().equals("IESCO") || tvBillTitle.getText().toString().contains("ISLAMABAD") )
        {
            //TODO
            //Update other fields in billText Node
        }
        else if(tvBillTitle.getText().toString().equals("SUI GAS") || tvBillTitle.getText().toString().contains("GAS") )
        {
            //TODO
            //Update other fields in billText Node
        }

        reference2.updateChildren(map2);


        Toasty.success(getContext(), "Updated Successfully!", Toast.LENGTH_LONG, true).show();

    }


    protected void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

}
