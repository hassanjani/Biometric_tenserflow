package com.example.facerecognition.QRcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.APPContstants;
import com.example.facerecognition.Activity.NidFetchActivity;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.example.facerecognition.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ScanFromGallary extends AppCompatActivity {
    SharedPreferences sh ;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_from_gallary);

//        InputStream is = new BufferedInputStream(new FileInputStream(file));
//        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        String decoded=scanQRImage(bitmap);
//        Log.i("QrTest", "Decoded string="+decoded);
        sh = this.getSharedPreferences("mypref", 0);

        Button pick=(Button) findViewById(R.id.buttonimg);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 111);

            }
        });



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
                        Toast.makeText(this, "The content of the QR image is: " + result.getText(), Toast.LENGTH_SHORT).show();
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



//        public static String scanQRImage(Bitmap bMap) {
//        String contents = null;
//
//        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
//        //copy pixel data from the Bitmap into the 'intArray' array
//        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
//
//        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//        Reader reader = new MultiFormatReader();
//        try {
//            Result result = reader.decode(bitmap);
//            contents = result.getText();
//        }
//        catch (Exception e) {
//            Log.e("QrTest", "Error decoding barcode", e);
//        }
//        return contents;
//    }


     public void verifyQrcode(Result intentData){
         try{
             key = sh.getString("key","qwertyuioplkjhgf");
             Log.d("msg key here",key+"");
             if(key!=null){
                 Log.d("msg intent data",intentData+"");
                 String dec= APPContstants.dycrypt(key,intentData.getText());


                 String shcyper =sh.getString("userjson","{name:null,email:null@gmail.com,phone:+null}");
                 if(shcyper!=null){
                     Log.d("cyphermsg sh",shcyper);
                     Log.d("cyphermsg dec",dec);
                     if(shcyper.equals(dec)){
                         Log.d("cy","matched");
//                                                Intent intentIdentify = new Intent(ScannedBarcodeActivity.this, FaceCaptureActivity.class);
//                                                intentIdentify.putExtra("activityType", AppSettings.TYPE_ACTIVIY_ENROLL);
//                                                startActivity(intentIdentify);


                         Toast.makeText(ScanFromGallary.this, "QR verified", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(ScanFromGallary.this, NidFetchActivity.class));

                     }else{
                         Log.d("cy","not matched");
                         Toast.makeText(ScanFromGallary.this, "QR not matched", Toast.LENGTH_SHORT).show();

                     }
                 }else{
                     Log.d("msg","cyper null");
                 }
             }else{
                 Log.d("msgException","key null");
             }
         }catch (Exception e){
             Log.d("msgException",e.toString());
         }

    }

}