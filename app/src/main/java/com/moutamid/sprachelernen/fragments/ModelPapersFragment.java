package com.moutamid.sprachelernen.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.PDFActivity;
import com.moutamid.sprachelernen.databinding.FragmentModelPapersBinding;

public class ModelPapersFragment extends Fragment {
    FragmentModelPapersBinding binding;

    public ModelPapersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentModelPapersBinding.inflate(getLayoutInflater(), container, false);

        binding.paper1.setOnClickListener(v -> startActivity(new Intent(requireContext(), PDFActivity.class).putExtra(Constants.PAPER, Constants.PAPER_1)));

        binding.paper2.setOnClickListener(v -> startActivity(new Intent(requireContext(), PDFActivity.class).putExtra(Constants.PAPER, Constants.PAPER_2)));

        binding.paper3.setOnClickListener(v -> startActivity(new Intent(requireContext(), PDFActivity.class).putExtra(Constants.PAPER, Constants.PAPER_3)));

        binding.paper4.setOnClickListener(v -> startActivity(new Intent(requireContext(), PDFActivity.class).putExtra(Constants.PAPER, Constants.PAPER_4)));

        return binding.getRoot();
    }
}