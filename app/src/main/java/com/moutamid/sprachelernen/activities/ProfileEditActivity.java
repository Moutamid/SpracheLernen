package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityProfileEditBinding;

public class ProfileEditActivity extends AppCompatActivity {
    ActivityProfileEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Update Profile");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

    }
}