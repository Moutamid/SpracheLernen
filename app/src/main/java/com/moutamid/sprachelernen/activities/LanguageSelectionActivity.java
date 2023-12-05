package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.MainActivity;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityLanguageSelectionBinding;

public class LanguageSelectionActivity extends AppCompatActivity {
    ActivityLanguageSelectionBinding binding;
    String selection = "ur";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Language Selection");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        boolean SHOW_TOOLBAR = getIntent().getBooleanExtra(Constants.SHOW_TOOLBAR, true);
        if (SHOW_TOOLBAR){
            binding.toolbar.getRoot().setVisibility(View.VISIBLE);
        }

        binding.next.setOnClickListener(v -> {
            // need to update the language in the firebase but right now there is only one language available
            // that's why there is no need to update just pass the user to next screen
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        binding.english.setOnClickListener(v -> showWarning());
        binding.persian.setOnClickListener(v -> showWarning());

    }

    private void showWarning() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("Language Unavailable")
                .setMessage("We're sorry, but the selected language is not available at the moment.")
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}