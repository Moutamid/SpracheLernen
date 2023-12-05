package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivitySettingsBinding;
import com.moutamid.sprachelernen.models.UserModel;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Settings");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.update.setOnClickListener(v -> startActivity(new Intent(this, ProfileEditActivity.class)));
        binding.changePassword.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel userModel = (UserModel) Stash.getObject(Constants.USER, UserModel.class);
        Glide.with(this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
        binding.username.setText(userModel.getName());
        binding.email.setText(userModel.getEmail());
    }
}