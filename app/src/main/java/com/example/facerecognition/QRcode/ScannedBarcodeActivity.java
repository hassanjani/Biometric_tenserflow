package com.example.facerecognition.QRcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.facerecognition.APPContstants;
import com.example.facerecognition.Activity.NidFetchActivity;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.example.facerecognition.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String MESSAGE_ALL_PERMISSIONS_GRANTED = "All permissions granted";
    private static final String WARNING_NOT_ALL_GRANTED = "Some permissions are not granted.";

    private static final String WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS = "Do you wish to proceed without granting all permissions?";

    private Map<String, Integer> mPermissions = new HashMap<String, Integer>();

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    SharedPreferences sh ;
    String key;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
//    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        //encrypt();

        setTitle("QR Scanner");

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        sh = this.getSharedPreferences("mypref", 0);


        Log.d("shared",""+sh.getString("key",null));
        Log.d("shared",""+sh.getString("name",null));
        Log.d("shared",""+sh.getString("email",null));
        Log.d("shared",""+sh.getString("phone",null));

//        btnAction = findViewById(R.id.btnAction);
//        btnAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (intentData.length() > 0) {
//                    APPContstants.dycrypt("Hassan khanhassa",intentData);
//
////                    startActivity(new Intent(ScannedBarcodeActivity.this, MainActivity.class));
//
////                    if (isEmail)
////                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
////                    else {
////                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
////                    }
//                }
//            }
//        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("MissingPermission")
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                Log.d("msg","if");
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                               // txtBarcodeValue.setText(intentData);
//                                isEmail = true;
//                                btnAction.setText("Verify");
                            } else {
                                Log.d("msg","else");
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                               // txtBarcodeValue.setText(intentData);
                                try{
                                    key = sh.getString("key","qwertyuioplkjhgf");
                                    Log.d("msg key here",key+"");
                                    if(key!=null){
                                        Log.d("msg intent data",intentData+"");
                                        String dec= APPContstants.dycrypt(key,intentData);


                                        String shcyper =sh.getString("userjson","{name:null,email:null@gmail.com,phone:+null}");
                                        if(shcyper!=null){
                                            Log.d("cyphermsg sh",shcyper);
                                            Log.d("cyphermsg dec",dec);
                                            if(shcyper.equals(dec)){
                                                Log.d("cy","matched");
//                                                Intent intentIdentify = new Intent(ScannedBarcodeActivity.this, FaceCaptureActivity.class);
//                                                intentIdentify.putExtra("activityType", AppSettings.TYPE_ACTIVIY_ENROLL);
//                                                startActivity(intentIdentify);


                                    Toast.makeText(ScannedBarcodeActivity.this, "QR verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ScannedBarcodeActivity.this, NidFetchActivity.class));

                                            }else{
                                                Log.d("cy","not matched");
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
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }



//    @SuppressLint("MissingSuperCall")
//    public void onRequestPermissionsResult(int requestCode, final String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
//                // Initialize the map with permissions
//                mPermissions.clear();
//                // Fill with actual results from user
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < permissions.length; i++) {
//                        mPermissions.put(permissions[i], grantResults[i]);
//
//                    }
//                    initialiseDetectorsAndSources();
//                    // Check if at least one is not granted
//                    if (mPermissions.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
////                            || mPermissions.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//
////                        showDialogOK(WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS,
////                                new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        switch (which) {
////                                            case DialogInterface.BUTTON_POSITIVE:
////                                                Log.w(LOG_TAG, WARNING_NOT_ALL_GRANTED);
////                                                for (Map.Entry<String, Integer> entry : mPermissions.entrySet()) {
////                                                    if (entry.getValue() != PackageManager.PERMISSION_GRANTED) {
////                                                        Log.w(LOG_TAG, entry.getKey() + ": PERMISSION_DENIED");
////                                                    }
////                                                }
////                                                new MainActivity.InitializationTask(MainActivity.this).execute();
////                                                break;
////                                            case DialogInterface.BUTTON_NEGATIVE:
////                                                requestPermissions(permissions);
////                                                break;
////                                            default:
////                                                throw new AssertionError("Unrecognised permission dialog parameter value");
////                                        }
////                                    }
////                                });
//                    } else {
//                        Log.i(LOG_TAG, MESSAGE_ALL_PERMISSIONS_GRANTED);
//                        initialiseDetectorsAndSources();
////                        new MainActivity.InitializationTask(this).execute();
//                    }
//                }
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }


}
