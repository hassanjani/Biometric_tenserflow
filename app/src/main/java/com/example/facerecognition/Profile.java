package com.example.facerecognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.Auth.Signin;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    TextView email,phone,matriculla, name;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        setTitle("Profile Details");

        email=(TextView) findViewById(R.id.editTextEmailAddress);
        matriculla=(TextView) findViewById(R.id.editTextMatriculla);
        name=(TextView) findViewById(R.id.editTextName);
        phone=(TextView) findViewById(R.id.editTextPhone);
        logout=(Button) findViewById(R.id.logout_button);
        SharedPreferences sh ;
        sh = this.getSharedPreferences("mypref", 0);
        String sh_email =sh.getString("email",null);
        String shmatriculla =sh.getString("matricula",null);
        String shname =sh.getString("name",null);
        String shphone =sh.getString("phone",null);

        email.setText(sh_email);
        matriculla.setText(shmatriculla);
        name.setText(shname);
        phone.setText(shphone);

       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               FirebaseAuth.getInstance()
                       .signOut();
               // user is now signed out
               Intent intent = new Intent(Profile.this, Signin.class);
               intent.putExtra("finish", true);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                       Intent.FLAG_ACTIVITY_CLEAR_TASK |
                       Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
           }
       });


    }
}