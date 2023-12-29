package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityLevelSelectionBinding;

public class LevelSelectionActivity extends BaseSecureActivity {
    ActivityLevelSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Learn Language");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.A1.setOnClickListener(v -> startActivity(new Intent(this, ExerciseListActivity.class).putExtra(Constants.LEVEL, "A1")));
        binding.A2.setOnClickListener(v -> startActivity(new Intent(this, ExerciseListActivity.class).putExtra(Constants.LEVEL, "A2")));
        binding.B1.setOnClickListener(v -> startActivity(new Intent(this, ExerciseListActivity.class).putExtra(Constants.LEVEL, "B1")));

    }
}