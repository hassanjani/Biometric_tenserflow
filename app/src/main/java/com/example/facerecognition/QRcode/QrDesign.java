package com.example.facerecognition.QRcode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.facerecognition.APPContstants;
import com.example.facerecognition.Activity.MainActivity;
import com.example.facerecognition.Activity.NidFetchActivity;
import com.example.facerecognition.Profile;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.example.facerecognition.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class QrDesign extends AppCompatActivity {

    ImageView qr,back,forward,profile;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SharedPreferences sh ;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_design);
        qr=(ImageView) findViewById(R.id.qrbtn);
        back=(ImageView) findViewById(R.id.qrbackbtn);
        forward=(ImageView) findViewById(R.id.qrforwardbtn);
        profile=(ImageView) findViewById(R.id.usericon);
        sh = this.getSharedPreferences("mypref", 0);
        try {
            if (ActivityCompat.checkSelfPermission(QrDesign.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {


            } else {
                ActivityCompat.requestPermissions(QrDesign.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showdialouge();
//                startActivity(new Intent(QrDesign.this,ScannedBarcodeActivity.class));
            }
        }); profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QrDesign.this, Profile.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               // startActivity(new Intent(QrDesign.this,ScannedBarcodeActivity.class));
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QrDesign.this,ScannedBarcodeActivity.class));
            }
        });




    }



    public void  showdialouge(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Chose option to Scan QR code");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(QrDesign.this,ScannedBarcodeActivity.class));

                    }
                });

        builder1.setNegativeButton(
                "Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent pickIntent = new Intent(Intent.ACTION_PICK);
                        pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, 111);

//                        startActivity(new Intent(QrDesign.this,ScanFromGallary.class));

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //the case is because you might be handling multiple request codes here
            case 111:
                if(data == null || data.getData()==null) {
                    Log.e("TAG", "The uri is null, probably the user cancelled the image selection process using the back button.");
                    return;
                }
                Uri uri = data.getData();
                try
                {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null)
                    {
                        Log.e("TAG", "uri is not a bitmap," + uri.toString());
                        return;
                    }
                    int width = bitmap.getWidth(), height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    bitmap.recycle();
                    bitmap = null;
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    MultiFormatReader reader = new MultiFormatReader();
                    try
                    {
                        Result result = reader.decode(bBitmap);
                        Log.d("msg content", result.getText());

//                        Toast.makeText(this, "The content of the QR image is: " + result.getText(), Toast.LENGTH_SHORT).show();
                        verifyQrcode(result);
                    }
                    catch (NotFoundException e)
                    {
                        Log.e("TAG", "decode exception", e);
                    }
                }
                catch (FileNotFoundException e)
                {
                    Log.e("TAG", "can not open file" + uri.toString(), e);
                }
                break;
        }
    }

    public void verifyQrcode(Result intentData){
        try{
            key = sh.getString("key","qwertyuioplkjhgf");
            Log.d("msg key here",key+"");
            if(key!=null){
                Log.d("msg intent data",intentData+"");
                String dec= APPContstants.dycrypt(key,intentData.getText());

                String shcyper =sh.getString("userjson","{name:null,email:null@gmail.com,phone:+null}");
                if(shcyper!=null && dec !=null){
                    Log.d("cyphermsg sh",shcyper);
                    Log.d("cyphermsg dec",dec);
                    if(shcyper.equals(dec)){
                        Log.d("cy","matched");
//                                                Intent intentIdentify = new Intent(ScannedBarcodeActivity.this, FaceCaptureActivity.class);
//                                                intentIdentify.putExtra("activityType", AppSettings.TYPE_ACTIVIY_ENROLL);
//                                                startActivity(intentIdentify);


                        Toast.makeText(QrDesign.this, "QR verified", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(QrDesign.this, NidFetchActivity.class));
                    }else{
                        Log.d("cy","not matched");
                        Toast.makeText(QrDesign.this, "QR not matched", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d("msg","cyper null");
                    Toast.makeText(QrDesign.this, "Sorry! QR not matched", Toast.LENGTH_SHORT).show();

                }
            }else{
                Log.d("msgException","key null");
            }
        }catch (Exception e){
            Log.d("msgException",e.toString());
        }

    }


}