package com.moutamid.sprachelernen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.sprachelernen.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity {
    ActivitySubscriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}