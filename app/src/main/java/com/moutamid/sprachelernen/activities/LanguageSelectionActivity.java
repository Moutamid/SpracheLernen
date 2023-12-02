package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityLanguageSelectionBinding;

public class LanguageSelectionActivity extends AppCompatActivity {
    ActivityLanguageSelectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}