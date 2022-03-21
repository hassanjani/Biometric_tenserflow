package com.example.facerecognition.Auth;

import static com.example.facerecognition.APPContstants.encrypt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facerecognition.QRcode.QrDesign;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.facerecognition.R;

import org.json.JSONObject;

public class Signin extends AppCompatActivity {

    EditText Edtemail, EdtPassword;
    ImageView Button;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        //Signup Button onClick Listener starts
        Edtemail = (EditText) findViewById(R.id.UserName);
        TextView textView = (TextView) findViewById(R.id.textView);
        EdtPassword = (EditText) findViewById(R.id.UserPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        ImageView button = (ImageView) findViewById(R.id.btn_signin);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Edtemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String email = Edtemail.getText().toString().trim();

                if (email.matches(emailPattern) && s.length() > 0) {
                    // Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or
                    textView.setText("");
                } else {
                    //Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                    textView.setText("invalid email");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Edtemail.getText().toString().trim().matches(emailPattern) || Edtemail.getText().length() < 8) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid Email",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    login();

                }

                // Do something in response to button click
                //   Toast.makeText(Signin.this, "Button SignIn Pressed", Toast.LENGTH_SHORT).show();
            }
        });
        //Signup Button onClick Listener ends

    }

    private void login() {


        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = Edtemail.getText().toString();
        password = EdtPassword.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // create new user or register new user
        mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Login successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            try {
                                FirebaseFirestore.getInstance().collection("user").document(task.getResult().getUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> taskdata) {

                                        try {


                                            SharedPreferences sh = getApplicationContext().getSharedPreferences("mypref", 0);
                                            SharedPreferences.Editor editor = sh.edit();
                                            editor.putString("name", taskdata.getResult().get("name").toString());
                                            editor.putString("email", taskdata.getResult().get("email").toString());
                                            editor.putString("matricula", taskdata.getResult().get("matricula").toString());
                                            editor.putString("phone", taskdata.getResult().get("phone").toString());
                                            JSONObject data = new JSONObject();
                                            data.put("name", taskdata.getResult().get("name").toString());
                                            data.put("email", taskdata.getResult().get("email").toString());
                                            data.put("phone", taskdata.getResult().get("phone").toString());
                                            editor.putString("userjson", data.toString());
                                            Log.d("shared",""+taskdata.getResult().get("name").toString(),null);
                                            Log.d("shared",""+taskdata.getResult().get("email").toString(),null);
                                            Log.d("shared",""+taskdata.getResult().get("phone").toString(),null);

                                            String key = taskdata.getResult().get("name").toString().trim() + taskdata.getResult().get("email").toString().trim();
                                            key = key.substring(0, 16);
                                            Log.d("sharedkey",""+key,null);

                                            editor.putString("key", key);
                                            editor.commit();
                                            editor.apply();


                                            //called encryption function
                                            encrypt(key, data.toString());
                                            //  byte[] a=  Contstants.encrypt(key,data.toString()) ;


                                        } catch (Exception e) {
                                            Log.d("msg", e.toString());
                                        }


                                    }
                                });

                            } catch (Exception e) {
                                Log.d("msg", e.toString());
                            }

                            progressBar.setVisibility(View.GONE);
                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(Signin.this,
                                    QrDesign.class);
                            startActivity(intent);
                            finish();


                        } else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Login failed!! " + task.getException().toString(),
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}