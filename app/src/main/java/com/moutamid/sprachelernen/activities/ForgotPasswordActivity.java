package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends BaseSecureActivity {
    ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Forgot Password");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.reset.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString())
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "A password reset link is sent to your email", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}