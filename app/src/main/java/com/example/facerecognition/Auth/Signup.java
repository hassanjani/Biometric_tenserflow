package com.example.facerecognition.Auth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.facerecognition.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {


    public EditText emailTextView, passwordTextView, nameTextview, phoneTextview,matricullaTextview;
    ImageView Btn;
    public ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String emailPattern;
    String passPattern;
    EditText CnfrmpasswordTextView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = (EditText) findViewById(R.id.editTextEmailAddress);
        TextView textView = (TextView) findViewById(R.id.textView2);
        TextView textViewpas = (TextView) findViewById(R.id.textView3);
        TextView textViewpascnfrm = (TextView) findViewById(R.id.textView4);
        passwordTextView = (EditText) findViewById(R.id.editTextPassword);
        CnfrmpasswordTextView = (EditText) findViewById(R.id.editTextCPasword);
        nameTextview = (EditText) findViewById(R.id.editTextName);
        phoneTextview = (EditText) findViewById(R.id.editTextPhone);
        matricullaTextview = (EditText) findViewById(R.id.editTextMatriculla);
        Btn = (ImageView) findViewById(R.id.btn_signUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        emailTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String email = emailTextView.getText().toString().trim();
                if (email.matches(emailPattern) && s.length() > 0) {
                    textView.setText("");
                } else {
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

        passPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        passwordTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String pass = passwordTextView.getText().toString().trim();
                if (pass.matches(passPattern) && s.length() > 0) {
                    textViewpas.setText("");
                } else {
                    textViewpas.setText("Password must contain one upper case letters, special charecter, and digits");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

        CnfrmpasswordTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String pass = passwordTextView.getText().toString().trim();
                String cpass = CnfrmpasswordTextView.getText().toString().trim();
                Log.d("msg", pass);
                Log.d("msg", cpass);
                if (pass.equals(cpass)) {
                    textViewpascnfrm.setText("");
                } else {
                    textViewpascnfrm.setText("Passwords must be matched");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
//
//        // taking FirebaseAuth instance

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        String email = emailTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();

        if (nameTextview.getText().length() < 2) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Full Name!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (phoneTextview.getText().length() < 7) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Phone no!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;

        } else if (!email.matches(emailPattern) || email.length() < 8) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;

        } else if (!password.matches(passPattern) || password.length() < 8) {
            Toast.makeText(getApplicationContext(),
                    "Password must have one upper case letters, special charecter, and digit",
                    Toast.LENGTH_LONG)
                    .show();
            return;

        } else if (!CnfrmpasswordTextView.getText().toString().trim().equals(password)) {
            Toast.makeText(getApplicationContext(),
                    "Passwords must be match",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        } else {


            // show the visibility of progress bar to show loading
            progressBar.setVisibility(View.VISIBLE);

            mAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                //    String email=editTextEmailAddress.getText().toString();
                                Map<String, Object> UserData = new HashMap<>();
                                UserData.put("name", nameTextview.getText().toString());
                                UserData.put("email", emailTextView.getText().toString());
                                UserData.put("phone", phoneTextview.getText().toString());
                                UserData.put("matricula", matricullaTextview.getText().toString());
                                String uid = task.getResult().getUser().getUid().toString();
                                Log.d("msg task", uid);
                                db.collection("user").document(uid).set(UserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),
                                                "Registration successful!",
                                                Toast.LENGTH_LONG)
                                                .show();
                                         try {
                                             SharedPreferences sh = getSharedPreferences("UserData", 0);
                                             SharedPreferences.Editor editor=sh.edit();
                                             editor.putString("name",nameTextview.getText().toString());
                                             editor.putString("email",emailTextView.getText().toString());
                                             editor.putString("matricula", matricullaTextview.getText().toString());
                                             editor.putString("phone",phoneTextview.getText().toString());
                                             JSONObject data = new JSONObject();
                                             data.put("name", nameTextview.getText().toString());
                                             data.put("email", emailTextView.getText().toString());
                                             data.put("phone", phoneTextview.getText().toString());
                                             editor.putString("userjson",data.toString());
                                             editor.commit();
                                             editor.apply();
                                             System.err.println(new String(data.toString()));


                                         }catch (Exception e){
                                             Log.d("msg",e.toString());
                                         }
                                        // hide the progress bar
                                        progressBar.setVisibility(View.GONE);
                                        // if the user created intent to login activity
                                        Intent intent
                                                = new Intent(Signup.this,
                                                Signin.class);
                                        startActivity(intent);
                                    }
                                });

                            } else {

                                // Registration failed
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
}