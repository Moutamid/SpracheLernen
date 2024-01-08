package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityLevelBinding;
import com.moutamid.sprachelernen.fragments.TopicsFragment;
import com.moutamid.sprachelernen.fragments.VoiceOversFragment;

import java.util.ArrayList;

public class LevelActivity extends BaseSecureActivity {
    ActivityLevelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Topics");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TopicsFragment()).commit();

    }

}