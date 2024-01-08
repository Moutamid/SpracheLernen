package com.moutamid.sprachelernen.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.FragmentVoiceOversBinding;

public class VoiceOversFragment extends Fragment {
    FragmentVoiceOversBinding binding;
    public VoiceOversFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVoiceOversBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }
}