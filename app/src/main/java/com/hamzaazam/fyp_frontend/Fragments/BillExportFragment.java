package com.hamzaazam.fyp_frontend.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamzaazam.fyp_frontend.Model.BillM;
import com.hamzaazam.fyp_frontend.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillExportFragment extends Fragment {

    public final static String BILLID_RECEIVE = "data_receive";
    String receivedBillId;

    Button btnConvertToPDF;
    Button btnShareToWhatsapp;
    Button btnShareText;


    String categoryToSend;
    String textToSend;

    DatabaseReference reference;
    String fuserid;

    public BillExportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_export, container, false);

        btnConvertToPDF=view.findViewById(R.id.btnPdf);
        btnShareToWhatsapp=view.findViewById(R.id.btnWhatsapp);
        btnShareText=view.findViewById(R.id.btnShareText);



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

        btnShareToWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: TEXT SHARE1
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                   toast("Whatsapp has not been installed.");
                }

            }
        });

        btnShareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///
                //TODO: TEXT SHARE2
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                ///
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
            //toast( receivedBillId+ " BillID");
        }
        else{
            receivedBillId="BILL ID was NULL";
            toast("Bill ID was NULL");
        }
    }





    public void saveBillTextToPdf(){
        final PdfDocument document = new PdfDocument();



        textToSend="BILL TEXT";
        textToSend+="\n";

        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedBillId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BillM bill = dataSnapshot.getValue(BillM.class);

                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {

                    categoryToSend=bill.getBillCategory();
                    categoryToSend+="\n";
                }
                if(!(bill.getBillAmount().equals("-")) && bill.getBillAmount()!=null) {
                    textToSend += "Bill Amount: ";
                    textToSend += bill.getBillAmount();
                    textToSend+="\n";

                }
                if(!(bill.getBillDate().equals("-")) && bill.getBillDate()!=null) {
                    textToSend += "Bill Date: ";
                    textToSend += bill.getBillDate();
                    textToSend+="\n";
                }
                if(!(bill.getBillCustomerName().equals("-")) && bill.getBillCustomerName()!=null) {
                    textToSend += "Bill Customer Name: ";
                    textToSend += bill.getBillCustomerName();
                    textToSend += "\n";
                }
                if(!(bill.getBillAddNote().equals("-")) && bill.getBillAddNote()!=null) {
                    textToSend+="Additional Note: ";
                    textToSend+=bill.getBillAddNote();
                    textToSend+="\n";
                }
                //TODO: ALSO WRITE OTHER BILL ATTR TO PDF
                if (bill.getBillCategory().equals("PTCL")){//hide units and meter no if category is ptcl

                }
                else{//Hide phone no card view if bill category is either iesco or sui gas

                }

                createAndDisplayPdf(textToSend,categoryToSend);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toast("DATABASE ERROR WHEN GETTING BILL DETAILS");
            }
        });


    }

    // Method for creating a pdf file from text, saving it then opening it for display
    public void createAndDisplayPdf(String text,String title) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir/";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "ocrTextFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font(Font.getFamily("COURIER"));
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);


            Toasty.info(getContext(), "PDF Made At Directory: "+path, Toast.LENGTH_LONG, true).show();


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {

            doc.close();
        }

       // viewPdf("newFile.pdf", "Dir");
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            toast("Cant Read PDF File");
        }
    }

    private void toast(String msg){
        Toast.makeText(getActivity(), msg,Toast.LENGTH_LONG).show();
    }

}


//    public void saveBillTextToPdf(){
//        final PdfDocument document = new PdfDocument();
//
//        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
//        // start a page
//        PdfDocument.Page page = document.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//
//
//
//        textToSend="BILL TEXT";
//        canvas.drawText(textToSend, 100, 60, paint);
//
//        fuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//
//        reference = FirebaseDatabase.getInstance().getReference("bills").child(fuserid).child(receivedBillId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                BillM bill = dataSnapshot.getValue(BillM.class);
//
//                if(!(bill.getBillCategory().equals("-")) && bill.getBillCategory()!=null) {
//
//                    categoryToSend=bill.getBillCategory();
//                    //canvas.drawText(categoryToSend, 30, 90, paint);
//                }
//                if(!(bill.getBillAmount().equals("-")) && bill.getBillAmount()!=null) {
//                    textToSend = "Bill Amount: ";
//                    textToSend += bill.getBillAmount();
//                    //canvas.drawText(textToSend, 30, 120, paint);
//
//                }
//                if(!(bill.getBillDate().equals("-")) && bill.getBillDate()!=null) {
//                    textToSend = "Bill Date: ";
//                    textToSend += bill.getBillDate();
//                    //canvas.drawText(textToSend, 30, 150, paint);
//
//                }
//                if(!(bill.getBillCustomerName().equals("-")) && bill.getBillCustomerName()!=null) {
//                    textToSend += "Bill Customer Name: ";
//                    textToSend += bill.getBillCustomerName();
//                    textToSend += "\n";
//                }
//                if(!(bill.getBillAddNote().equals("-")) && bill.getBillAddNote()!=null) {
//                    textToSend.concat("Additional Note: ");
//                    textToSend.concat(bill.getBillAddNote());
//                    textToSend.concat("\n");                }
//
//                if (bill.getBillCategory().equals("PTCL")){//hide units and meter no if category is ptcl
//
//                }
//                else{//Hide phone no card view if bill category is either iesco or sui gas
//
//                }
//
//
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                toast("DATABASE ERROR WHEN GETTING BILL DETAILS");
//            }
//        });
//
//        // finish the page
//        document.finishPage(page);
//        createPdf(textToSend,categoryToSend,document);
//    }

//    //convert text to pdf
//    private void createPdf(String sometext, String title, PdfDocument document){
//        // create a new document
//
//        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
//        // start a page
//        PdfDocument.Page page = document.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        canvas.drawCircle(50, 50, 30, paint);
//        paint.setColor(Color.BLACK);
//        canvas.drawText(sometext, 80, 50, paint);
//        //canvas.drawt
//        // finish the page
//        document.finishPage(page);
//// draw text on the graphics object of the page
//        // Create Page 2
//        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
//        page = document.startPage(pageInfo);
//        canvas = page.getCanvas();
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(100, 100, 100, paint);
//        document.finishPage(page);
//        // write the document content
//        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
//        File file = new File(directory_path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String targetPdf = directory_path+"test-2.pdf";
//        File filePath = new File(targetPdf);
//        try {
//            document.writeTo(new FileOutputStream(filePath));
//            toast("PDF MADE AT PATH "+directory_path);
//        } catch (IOException e) {
//            Log.e("main", "error "+e.toString());
//            toast("ERROR IN CONVERITNG"+e.toString());
//        }
//        // close the document
//        document.close();
//    }

