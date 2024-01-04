package com.moutamid.sprachelernen.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.LevelActivity;
import com.moutamid.sprachelernen.activities.LevelSelectionActivity;
import com.moutamid.sprachelernen.activities.VocabularyActivity;
import com.moutamid.sprachelernen.activities.WritingActivity;
import com.moutamid.sprachelernen.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        binding.level.setOnClickListener(v -> startActivity(new Intent(requireContext(), LevelSelectionActivity.class)));
        binding.speaking.setOnClickListener(v -> {
            Stash.put(Constants.TOPIC, Constants.Speaking);
            startActivity(new Intent(requireContext(), LevelActivity.class));
        });
        binding.writing.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), WritingActivity.class));
        });
        binding.reading.setOnClickListener(v -> {
            Stash.put(Constants.TOPIC, Constants.Reading);
            startActivity(new Intent(requireContext(), LevelActivity.class));
        });
        binding.vocabulary.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), VocabularyActivity.class));
        });

        return binding.getRoot();
    }
}