package com.example.facerecognition;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        setTitle("Welcome");
        profile=(ImageView) findViewById(R.id.usericon);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinalActivity.this,Profile.class));
            }
        });
    }




    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    setResult(RESULT_CANCELED);

                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

//                    Log.e(LOG_TAG, "before onBackPressed : License status -  " +  LicensingManager.getInstance().isLicensesObtained());
//                    LicensingManager.getInstance().release();
//                    Log.e(LOG_TAG, "after onBackPressed : License status -  " +  LicensingManager.getInstance().isLicensesObtained());
//                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {})
                .setOnCancelListener(dialog -> {})
                .show();
    }
}