package com.hamzaazam.fyp_frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import me.pqpo.smartcropperlib.view.CropImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ocrActivity extends AppCompatActivity {
    ////////camera variables
    private static final String IMAGE_DIRECTORY = "/YourDirectName";
    private Context mContext;
    private CropImageView billImageView;  // imageview
    private int GALLERY = 1, CAMERA = 2;

    Bitmap billImageBitmap;
    ///////


    EditText ipv4AddressView ;
    EditText portNumberView;


    Button btnSelect;
    Button btnCrop;

    String ocrText;//raw json string with brackets
    String jsonToString;//will convert json object to string array

    public Toolbar toolbar;


    Button btnSeeText;
    Boolean isTextExtracted=false;


    WifiManager wifiManager;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" OCR BILL");
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///////////
        ipv4AddressView = findViewById(R.id.IPAddress);
        portNumberView = findViewById(R.id.portNumber);
        btnSelect=findViewById(R.id.btnSelect);
        btnCrop=findViewById(R.id.btnCrop);
        //////////


        //////////
        requestMultiplePermissions();
        billImageView = findViewById(R.id.billimage);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        ////

        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap crop = billImageView.crop();
                billImageView.setImageBitmap(crop);
                billImageBitmap=((BitmapDrawable)billImageView.getDrawable()).getBitmap();
            }
        });

        btnSeeText=findViewById(R.id.btnSeeText);
        btnSeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTextExtracted==true){
                    Intent intent=new Intent(getApplicationContext(),OcrTextDialogActivity.class);
                    intent.putExtra("ocrtext",jsonToString);
                    startActivity(intent);
                }
                else{
                    Toasty.info(getApplicationContext(), "No Extracted Text", Toast.LENGTH_LONG, true).show();
                }
            }
        });

    }


    public void connectServer(View v) {
        //EditText ipv4AddressView = findViewById(R.id.IPAddress);
        String ipv4Address = ipv4AddressView.getText().toString();
        //EditText portNumberView = findViewById(R.id.portNumber);
        String portNumber = portNumberView.getText().toString();


        String postUrl = "http://" + ipv4Address + ":" + portNumber + "/";

        //2
        //

        ///Selected image is read as a bitmap
        ByteArrayOutputStream stream = new ByteArrayOutputStream();//First, the Bitmap is converted into a byte stream
        //using a byte stream helps to avoid storing all of the data into memory when uploading it

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Read BitMap by file path
        if(billImageBitmap==null){
            Bitmap tempBit = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.p11_31);
            billImageBitmap=tempBit;
        }
        else {
            billImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byte[] byteArray = stream.toByteArray();

        //MultipartBody class, which supports sending multi-part data in the HTTP.
        //  Field name: Field name that will be used at the server to retrieve the file.
        //  Filename: The filename of the uploaded file.
        //  Body: The content of the body as a RequestBody instance
        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        TextView responseText = findViewById(R.id.responseText);
        responseText.setText("Please wait ...");

        postRequest(postUrl, postBodyImage, v);
    }

    void postRequest(String postUrl, RequestBody postBody, final View v) {

        ///////////
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(120000, TimeUnit.MILLISECONDS);
        b.writeTimeout(15000, TimeUnit.MILLISECONDS);

        OkHttpClient client2 = b.build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client2.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                Log.e("ERROR CON HAX",e.toString());
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);
                        responseText.setText("Failed to Connect to Server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);
                        try {

                            ocrText=response.body().string();

                            //String converted to JSON Object
                            JSONObject jsonText = new JSONObject(ocrText);
                            Log.d("JSON_TEXT", jsonText.toString());
                            responseText.setText("Ocr Text Received");

                            //Iterating over Json Object and converting it to a String
                            Iterator<String> iter = jsonText.keys();
                            jsonToString="";
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    Object value = jsonText.get(key);
                                    jsonToString+=value.toString();
                                    jsonToString+="\n --------- \n";
                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                            }


                            ////////////////////////////ADDING RECEIVED TEXT TO FIREBASE
                            //Note: View added in this func para

                            //enabling wifi
                            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            enableWifi(v);


                            String fuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference reference2= FirebaseDatabase.getInstance().getReference("bills").child(fuserid);
                            HashMap<String ,Object> map2 =new HashMap<>();
//                            if(profileAbout.getText()!=null) {
//                                map2.put("about", profileAbout.getText().toString());
//                            }
//                            if(profileAge.getText()!=null && !(profileAge.getText().toString().equals(" ")) &&
//                                    !(profileAge.getText().toString().equals(""))) {
//                                map2.put("age", Integer.parseInt(profileAge.getText().toString()));
//                            }
//
//                            if(listArray!=null) {
//                                map2.put("listSportPreferences", listArray);
//                            }

                            Map<String,Object> jsonMap=toMap(jsonText);
                            if(jsonMap.containsKey("Amount")){
                                if(jsonMap.get("Amount").toString()!=null){
                                    map2.put("billAmount", jsonMap.get("Amount"));
                                }
                                else{
                                    map2.put("billAmount", "-");
                                }
                            }
                            if(jsonMap.containsKey("Title")){
                                if(jsonMap.get("Title").toString()!=null){
                                    map2.put("billCateogry", jsonMap.get("Title"));
                                }
                                else{
                                    map2.put("billCategory", "-");
                                }
                            }
                            if(jsonMap.containsKey("Name")){
                                if(jsonMap.get("Name").toString()!=null){
                                    map2.put("billCustomerName", jsonMap.get("Name"));
                                }
                                else{
                                    map2.put("billCustomerName", "-");
                                }
                            }
                            if(jsonMap.containsKey("Date")){
                                if(jsonMap.get("Date").toString()!=null){
                                    map2.put("billDate", jsonMap.get("Date"));
                                }
                                else{
                                    map2.put("billDate", "-");
                                }
                            }
                            map2.put("billAddNote", "-");

                            //TODO: SET IMAGE URL
                            map2.put("billImageUrl", "None Chosen");

                            map2.put("billText", jsonMap);

                            reference2.push().setValue(map2);


                            Toasty.success(getApplicationContext(), "Bill Added Successfully!", Toast.LENGTH_LONG, true).show();



                            ////////////////////////////

                            isTextExtracted=true;
                            Intent intent=new Intent(getApplicationContext(),OcrTextDialogActivity.class);
                            intent.putExtra("ocrtext",jsonToString);
                            startActivity(intent);


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        /////////1
        ////////

    }





    /////////////////CAMERA & GALLERY METHODS

    ///Show options dialog box (to select image from camera or gallery)
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    //get photo from gallery
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    //get photo from camera
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    //once image is taken or selected :
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved G!", Toast.LENGTH_SHORT).show();
                    billImageBitmap=bitmap;////////////////
                    billImageView.setImageBitmap(bitmap);

                    //crop
                    billImageView.setImageToCrop(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            billImageBitmap=thumbnail;///////////
            billImageView.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved C!", Toast.LENGTH_SHORT).show();
        }
    }

    //store the pic
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    //request permission
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    //Turns on wifi
    public void enableWifi(View view){
        wifiManager.setWifiEnabled(true);
        Toast.makeText(this, "Wifi enabled", Toast.LENGTH_SHORT).show();
    }
    //disables wifi
    public void disableWifi(View view){
        wifiManager.setWifiEnabled(false);
        Toast.makeText(this, "Wifi Disabled", Toast.LENGTH_SHORT).show();
    }

    //converts json obj to map
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


}



////1
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(postBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Cancel the post on failure.
//                Log.e("ERROR CON HAX",e.toString());
//                call.cancel();
//
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView responseText = findViewById(R.id.responseText);
//                        responseText.setText("Failed to Connect to Server");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView responseText = findViewById(R.id.responseText);
//                        try {
//                            responseText.setText(response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });


/////2
//        String postBodyText = "Hello";
//        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
//        RequestBody postBody = RequestBody.create(mediaType, postBodyText);
//
//        postRequest(postUrl, postBody);
