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

import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityLevelBinding;
import com.moutamid.sprachelernen.fragments.TopicsFragment;
import com.moutamid.sprachelernen.fragments.VoiceOversFragment;

import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity {
    ActivityLevelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String level = getIntent().getStringExtra(Constants.LEVEL);

        binding.toolbar.title.setText(level);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new TopicsFragment(), "Topics List");
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