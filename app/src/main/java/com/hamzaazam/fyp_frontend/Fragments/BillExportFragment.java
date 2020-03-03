package com.hamzaazam.fyp_frontend.Fragments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hamzaazam.fyp_frontend.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillExportFragment extends Fragment {

    public final static String BILLID_RECEIVE = "data_receive";
    String receivedBillId;

    Button btnConvertToPDF;



    public BillExportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_export, container, false);

        btnConvertToPDF=view.findViewById(R.id.btnPdf);



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
