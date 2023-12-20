package com.moutamid.sprachelernen.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.TopicsListAdapter;
import com.moutamid.sprachelernen.databinding.FragmentTopicsBinding;
import com.moutamid.sprachelernen.models.TopicsModel;
import com.moutamid.sprachelernen.models.UserModel;

import java.util.ArrayList;

public class TopicsFragment extends Fragment {
    FragmentTopicsBinding binding;

    ArrayList<TopicsModel> list;

    public TopicsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopicsBinding.inflate(getLayoutInflater(), container, false);

        binding.topicsRC.setHasFixedSize(false);
        binding.topicsRC.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();

        return binding.getRoot();
    }

    private String getLanguage() {
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        String lang = Constants.URDU;

        if (userModel.getLanguage().equals("ur")){
            lang = Constants.URDU;
        }

        return lang;
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
        Constants.showDialog();
        String topic = Stash.getString(Constants.TOPIC, Constants.Speaking);
        Constants.databaseReference().child(getLanguage()).child(Constants.TOPICS).child(topic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Constants.dismissDialog();
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TopicsModel model = dataSnapshot.getValue(TopicsModel.class);
                        list.add(model);
                    }
                    TopicsListAdapter adapter = new TopicsListAdapter(requireContext(), list);
                    binding.topicsRC.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.dismissDialog();
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}