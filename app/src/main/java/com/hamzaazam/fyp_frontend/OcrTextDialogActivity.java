package com.hamzaazam.fyp_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class OcrTextDialogActivity extends AppCompatActivity {
    String ocrTextString;
    TextView tvOcrText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ocr_text_dialog);
        getWindow().setLayout(1000,1000);


        Intent intent=getIntent();
        ocrTextString=intent.getStringExtra("ocrtext");

        tvOcrText=findViewById(R.id.ocrtext);
        tvOcrText.setText(ocrTextString);

    }
}
