package com.moutamid.sprachelernen.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.LevelSelectionActivity;
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

        return binding.getRoot();
    }
}