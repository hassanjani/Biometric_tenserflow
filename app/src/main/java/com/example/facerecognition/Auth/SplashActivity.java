package com.example.facerecognition.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.Activity.NidFetchActivity;
import com.example.facerecognition.R;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        FirebaseApp.initializeApp(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent=new Intent(SplashActivity.this, ScanFromGallary.class);
                Intent intent=new Intent(SplashActivity.this, MainActivity1.class);
//                Intent intent=new Intent(SplashActivity.this, NidFetchActivity.class);
                startActivity(intent);
                finish();

            }
        }, 2500);
    }
}