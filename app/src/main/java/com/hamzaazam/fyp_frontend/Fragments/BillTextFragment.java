package com.hamzaazam.fyp_frontend.Fragments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private EditText billPhoneNo;
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
        billPhoneNo=view.findViewById(R.id.phoneNo);

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

                HashMap<String,Object> billText=bill.getBillText();

                if(bill.getBillCategory()!=null) {
                    tvBillTitle.setText(bill.getBillCategory());
                }
                if( bill.getBillAmount()!=null) {
                    billAmount.setText(bill.getBillAmount());
                }
                if(bill.getBillDate()!=null) {
                    billDate.setText(bill.getBillDate());
                }
                if( bill.getBillCustomerName()!=null) {
                    billCustomerName.setText(bill.getBillCustomerName());
                }
                if( bill.getBillAddNote()!=null) {
                    billAddNote.setText(bill.getBillAddNote());
                }
                if (bill.getBillCategory().equals("PTCL")){//hide units and meter no if category is ptcl
                    cvMno_Units.setVisibility(View.GONE);

                    if(billText.containsKey("PhoneNumber")) {
                        if(billText.get("PhoneNumber").toString() !=null) {
                            billPhoneNo.setText(billText.get("PhoneNumber").toString());
                        }
                    }
                }
                else{//Hide phone no card view if bill category is either iesco or sui gas
                    cvPno.setVisibility(View.GONE);

                    if(billText.containsKey("Units")) {
                        if (billText.get("Units").toString() !=null) {
                            billUnits.setText(billText.get("Units").toString());
                        }
                    }
                    if(billText.containsKey("Meter")) {
                        if (billText.get("Meter").toString() !=null) {
                            billMeterNo.setText(billText.get("Meter").toString());
                        }
                    }
                }
                if(billText.containsKey("Address")) {
                    if(billText.get("Address").toString() !=null) {
                        billCustomerAddress.setText(billText.get("Address").toString());
                    }
                }

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

        HashMap<String ,Object> billTextMap =new HashMap<>();

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
            //else cond check
            if(billCustomerAddress.getText().toString()!=null && !(billCustomerAddress.getText().toString().equals("")))
            {
                billTextMap.put("Address",billCustomerAddress.getText().toString());
            }
            if(billAmount.getText().toString()!=null && !(billAmount.getText().toString().equals("")))
            {
                billTextMap.put("Amount",billAmount.getText().toString());
            }
            if(billDate.getText().toString()!=null && !(billDate.getText().toString().equals("")))
            {
                billTextMap.put("Date",billDate.getText().toString());
            }
            if(billCustomerName.getText().toString()!=null && !(billCustomerName.getText().toString().equals("")))
            {
                billTextMap.put("Name",billCustomerName.getText().toString());
            }
            if(billPhoneNo.getText().toString()!=null && !(billPhoneNo.getText().toString().equals("")))
            {
                billTextMap.put("PhoneNumber",billPhoneNo.getText().toString());
            }
            billTextMap.put("Title",tvBillTitle.getText().toString());

            map2.put("billText",billTextMap);
        }
        else if((tvBillTitle.getText().toString().equals("IESCO") || tvBillTitle.getText().toString().contains("ISLAMABAD"))
        ||(tvBillTitle.getText().toString().equals("SUI GAS") || tvBillTitle.getText().toString().contains("GAS")))
        {
            //TODO
            //Update other fields in billText Node
            //else cond check
            if(billCustomerAddress.getText().toString()!=null && !(billCustomerAddress.getText().toString().equals("")))
            {
                billTextMap.put("Address",billCustomerAddress.getText().toString());
            }
            if(billAmount.getText().toString()!=null && !(billAmount.getText().toString().equals("")))
            {
                billTextMap.put("Amount",billAmount.getText().toString());
            }
            if(billDate.getText().toString()!=null && !(billDate.getText().toString().equals("")))
            {
                billTextMap.put("Date",billDate.getText().toString());
            }
            if(billMeterNo.getText().toString()!=null && !(billMeterNo.getText().toString().equals("")))
            {
                billTextMap.put("Meter",billMeterNo.getText().toString());
            }
            if(billCustomerName.getText().toString()!=null && !(billCustomerName.getText().toString().equals("")))
            {
                billTextMap.put("Name",billCustomerName.getText().toString());
            }

            billTextMap.put("Title",tvBillTitle.getText().toString());

            if(billUnits.getText().toString()!=null && !(billUnits.getText().toString().equals("")))
            {
                billTextMap.put("Units",billUnits.getText().toString());
            }
            map2.put("billText",billTextMap);
        }


        reference2.updateChildren(map2);


        Toasty.success(getContext(), "Updated Successfully!", Toast.LENGTH_LONG, true).show();

    }


    protected void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
    }

}
