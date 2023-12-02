package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.create.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
        binding.create.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }
}