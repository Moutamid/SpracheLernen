package com.moutamid.sprachelernen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.moutamid.sprachelernen.activities.WelcomeActivity;
import com.moutamid.sprachelernen.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivitySubscriptionBinding binding;
    String selectedPlan = Constants.VIP_YEAR;
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        binding.annual.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_YEAR;
            updateAnnual();
        });

        binding.half.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_6_MONTH;
            updateHalf();
        });

        binding.month.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_3_MONTH;
            updateMonth();
        });

        binding.start.setOnClickListener(v -> {
            bp.subscribe(SubscriptionActivity.this, selectedPlan);
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

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}