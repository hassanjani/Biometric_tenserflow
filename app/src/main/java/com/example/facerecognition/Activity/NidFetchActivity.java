package com.example.facerecognition.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facerecognition.FaceDetection.Box;
import com.example.facerecognition.FaceDetection.MTCNN;
import com.example.facerecognition.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;


public class NidFetchActivity extends AppCompatActivity {
    private EditText etNid;
    SharedPreferences sh;


    private ImageButton btnEnroll;
    private ImageButton btnIdentify;
    private ImageButton btnCleanDB;

    Bitmap serverImage;
    String encodedImage;
    byte[] decodedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniVariable();


        //hassan
        encodedImage="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";


//        ivNidImage.setImageBitmap(serverImage);


       btnEnroll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent pickIntent = new Intent(Intent.ACTION_PICK);
               pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
               startActivityForResult(pickIntent, 111);
           }
       });

       btnCleanDB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SharedPreferences.Editor editor = sh.edit();
               editor.putString("image", null);
               editor.commit();
               editor.apply();
               btnEnroll.setEnabled(true);
               btnEnroll.setImageAlpha(0xFF);


           }
       });



        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Convert to byte array

                String shimage=sh.getString("image",null);
                if(shimage!=null){
                    encodedImage=shimage;
                }

                decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                serverImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                serverImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(NidFetchActivity.this, CameraViewActivity.class);
                intent.putExtra("harun",encodedImage);
                startActivity(intent);
                finish();
            }
        });
    }

    private void iniVariable() {

        btnEnroll = findViewById(R.id.btnEnroll);
        btnIdentify = findViewById(R.id.btnIdentify);
        btnCleanDB = findViewById(R.id.btnCleanDB);
        sh = getApplicationContext().getSharedPreferences("mypref", 0);
        btnEnroll.setEnabled(false);
        btnEnroll.setImageAlpha(0x3F);



        if(sh.getString("image",null)==null){
            btnEnroll.setEnabled(true);
            btnEnroll.setImageAlpha(0xFF);

        }


//        etNid = findViewById(R.id.etNid);
//        ivNidImage = findViewById(R.id.ivNidImage);
//        btnVerify = findViewById(R.id.btnVerify);
//        btnpickimage = findViewById(R.id.btnpickimage);
    }


    public static String getFileToByte(Bitmap bmp){
//        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
//            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
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
                Log.d("msgpath",""+uri);
                Log.d("msgpath",""+uri.getPath());


                try
                {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap == null)
                    {
                        Log.e("TAG", "uri is not a bitmap," + uri.toString());
                        return;
                    }
                    MTCNN mtcnn = new MTCNN(getAssets());

                    bitmap= cropFace(bitmap, mtcnn);
                    mtcnn.close();


                    encodedImage=getFileToByte(bitmap);
                    Log.d("msgpathbyte",""+encodedImage);

                    decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    serverImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("image", encodedImage);
                    editor.commit();
                    editor.apply();

                    btnEnroll.setEnabled(false);
                    btnEnroll.setImageAlpha(0x3F);



//                    ivNidImage.setImageBitmap(serverImage);



//                    int width = bitmap.getWidth(), height = bitmap.getHeight();
//                    int[] pixels = new int[width * height];
//                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//                    bitmap.recycle();
//                    bitmap = null;
//                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
//                    BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
//                    MultiFormatReader reader = new MultiFormatReader();
//                    try
//                    {
//                        Result result = reader.decode(bBitmap);
//                        Toast.makeText(this, "The content of the QR image is: " + result.getText(), Toast.LENGTH_SHORT).show();
//                        verifyQrcode(result);
//                    }
//                    catch (NotFoundException e)
//                    {
//                        Log.e("TAG", "decode exception", e);
//                    }
                }
                catch (FileNotFoundException e)
                {
                    Log.e("TAG", "can not open file" + uri.toString(), e);
                }
                break;
        }
    }


    private Bitmap cropFace(Bitmap bitmap, MTCNN mtcnn){
        Bitmap croppedBitmap = null;
        try {
            Vector<Box> boxes = mtcnn.detectFaces(bitmap, 10);

            Log.i("MTCNN", "No. of faces detected: " + boxes.size());

            int left = boxes.get(0).left();
            int top = boxes.get(0).top();

            int x = boxes.get(0).left();
            int y = boxes.get(0).top();
            int width = boxes.get(0).width();
            int height = boxes.get(0).height();


            if (y + height >= bitmap.getHeight())
                height -= (y + height) - (bitmap.getHeight() - 1);
            if (x + width >= bitmap.getWidth())
                width -= (x + width) - (bitmap.getWidth() - 1);

            Log.i("MTCNN", "Final x: " + String.valueOf(x + width));
            Log.i("MTCNN", "Width: " + bitmap.getWidth());
            Log.i("MTCNN", "Final y: " + String.valueOf(y + width));
            Log.i("MTCNN", "Height: " + bitmap.getWidth());

            croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        }catch (Exception e){
            e.printStackTrace();
        }
        return croppedBitmap;
    }

}