package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Change Password");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.change.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                FirebaseUser user = Constants.auth().getCurrentUser();
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), binding.oldPassword.getEditText().getText().toString());
                user.reauthenticate(authCredential).addOnSuccessListener(unused -> {
                    user.updatePassword(binding.newPassword.getEditText().getText().toString())
                            .addOnSuccessListener(unused1 -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Current Password Doesn't Match", Toast.LENGTH_SHORT).show();
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
        if (binding.newPassword.getEditText().getText().toString().isEmpty()) {
            binding.newPassword.getEditText().setError("Required*");
            return false;
        }
        if (binding.rePassword.getEditText().getText().toString().isEmpty()) {
            binding.rePassword.getEditText().setError("Required*");
            return false;
        }
        if (!binding.newPassword.getEditText().getText().toString().equals(binding.rePassword.getEditText().getText().toString())) {
            binding.rePassword.getEditText().setError("Password didn't match*");
            return false;
        }
        if (binding.oldPassword.getEditText().getText().toString().isEmpty()) {
            binding.oldPassword.getEditText().setError("Required*");
            return false;
        }
        return true;
    }
}