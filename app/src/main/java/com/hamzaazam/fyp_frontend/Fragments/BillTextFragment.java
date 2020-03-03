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
    String receivedBillId;

    View vieww;
    ////Setting & getting profile data
    DatabaseReference reference;
    String fuserid;

    CardView cvMno_Units;
    CardView cvPno;


    ///
    TextView tvBillTitle;
    EditText billAmount;
    EditText billDate;
    EditText billCustomerName;
    EditText billCustomerAddress;
    EditText billAddNote;
    EditText billMeterNo;
    EditText billUnits;
    ///

    Button btnSaveData;
    ImageButton btnConvertToPDF;


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

        btnConvertToPDF=view.findViewById(R.id.btnPdf);
        btnSaveData=view.findViewById(R.id.btnSaveData);


        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });

        btnConvertToPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBillTextToPdf();
            }
        });

        btnConvertToPDF.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toast("Save Bill Text as PDF");
                return true;
            }
        });


        return view;

    }


    //In this function we receive the bill id which was clicked by the user
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            receivedBillId=args.getString(BILLID_RECEIVE);
            toast( receivedBillId+ " BillID");
        }
        else{
            receivedBillId="BILL ID was NULL";
            toast("Bill ID was NULL");
        }

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

    public void saveBillTextToPdf(){


    }

    //convert text to pdf
    private void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            toast("PDF MADE");
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            toast("ERROR IN CONVERITNG"+e.toString());
        }
        // close the document
        document.close();
    }


    private void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();
    }

}
