package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityModelPaperBinding;
import com.moutamid.sprachelernen.fragments.ModelPapersFragment;
import com.moutamid.sprachelernen.fragments.TopicsFragment;
import com.moutamid.sprachelernen.fragments.VoiceOversFragment;

import java.util.ArrayList;

public class ModelPaperActivity extends AppCompatActivity {
    ActivityModelPaperBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModelPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Model Papers");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new ModelPapersFragment(), "Papers");
        adapter.addFragment(new VoiceOversFragment(), "Voice Overs");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);


    }

    public static class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragmentList = new ArrayList<>();
        private ArrayList<String> fragmentTitles = new ArrayList<>();

        public ViewPagerFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

}