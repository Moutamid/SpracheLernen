package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityModelPaperBinding;

public class ModelPaperActivity extends BaseSecureActivity {
    ActivityModelPaperBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModelPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.toolbar.title.setText("Model Papers");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        Toast.makeText(this, "Model paper is not available", Toast.LENGTH_LONG).show();

    }
}