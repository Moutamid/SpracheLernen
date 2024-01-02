package com.moutamid.sprachelernen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.sprachelernen.activities.TrialScreenActivity;
import com.moutamid.sprachelernen.activities.WelcomeActivity;

public class SplashScreenActivity extends BaseSecureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            if (Constants.checkInternet(SplashScreenActivity.this)){
                if (Constants.auth().getCurrentUser() != null){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, TrialScreenActivity.class));
                    finish();
                }
            } else {
                new MaterialAlertDialogBuilder(SplashScreenActivity.this)
                        .setTitle("No Internet")
                        .setMessage("Check your internet connection")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        })
                        .show();
            }
        }, 2000);

    }

}