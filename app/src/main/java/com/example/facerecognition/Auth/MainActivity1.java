package com.example.facerecognition.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.R;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ActionBar actionBar = getSupportActionBar();
      //  actionBar.hide();


        //Btn to trigger here for signup and signin
        ImageView button_signup=(ImageView) findViewById(R.id.btn_signup1);
        ImageView button_signin=(ImageView) findViewById(R.id.btn_signin);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity1.this, Signup.class));

            }
        });

        button_signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                startActivity(new Intent(MainActivity1.this, Signin.class));

               /* Intent intent2=new Intent(MainActivity.this, Signin.class);
                StartActivity(intent2);*/
            }
        });



    }
}