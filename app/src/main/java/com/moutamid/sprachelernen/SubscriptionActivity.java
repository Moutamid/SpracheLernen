package com.moutamid.sprachelernen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.sprachelernen.activities.WelcomeActivity;
import com.moutamid.sprachelernen.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity {
    ActivitySubscriptionBinding binding;
    String selectedPlan = "annual";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        binding.annual.setOnClickListener(v -> {
            selectedPlan = "annual";
            updateAnnual();
        });

        binding.half.setOnClickListener(v -> {
            selectedPlan = "6_Month";
            updateHalf();
        });

        binding.month.setOnClickListener(v -> {
            selectedPlan = "3_Month";
            updateMonth();
        });

        binding.start.setOnClickListener(v -> {

        });

    }

    private void updateMonth() {
        binding.annual.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.annual.setStrokeColor(getResources().getColor(R.color.grey));
        binding.half.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.half.setStrokeColor(getResources().getColor(R.color.grey));
        binding.month.setCardBackgroundColor(getResources().getColor(R.color.greenLight));
        binding.month.setStrokeColor(getResources().getColor(R.color.greenDark));
    }

    private void updateHalf() {
        binding.annual.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.annual.setStrokeColor(getResources().getColor(R.color.grey));
        binding.half.setCardBackgroundColor(getResources().getColor(R.color.greenLight));
        binding.half.setStrokeColor(getResources().getColor(R.color.greenDark));
        binding.month.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.month.setStrokeColor(getResources().getColor(R.color.grey));
    }

    private void updateAnnual() {
        binding.annual.setCardBackgroundColor(getResources().getColor(R.color.greenLight));
        binding.annual.setStrokeColor(getResources().getColor(R.color.greenDark));
        binding.half.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.half.setStrokeColor(getResources().getColor(R.color.grey));
        binding.month.setCardBackgroundColor(getResources().getColor(R.color.grey));
        binding.month.setStrokeColor(getResources().getColor(R.color.grey));
    }

}