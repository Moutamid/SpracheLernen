package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityCongratulationBinding;

public class CongratulationActivity extends BaseSecureActivity {
    ActivityCongratulationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCongratulationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 0);

        binding.correct.setText(correct + "");
        binding.incorrect.setText((total - correct) + "");
        double t = 100/total;
        double score = t * correct;

        binding.total.setText(String.format("%.2f", score));

        binding.continueBtn.setOnClickListener(v -> onBackPressed());

    }
}