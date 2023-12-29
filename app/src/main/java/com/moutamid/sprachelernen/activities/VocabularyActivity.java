package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.ModelTopicsAdapter;
import com.moutamid.sprachelernen.databinding.ActivityVocabularyBinding;
import com.moutamid.sprachelernen.models.TopicsModel;

import java.util.ArrayList;
import java.util.Collections;

public class VocabularyActivity extends AppCompatActivity {
    ActivityVocabularyBinding binding;
    ArrayList<TopicsModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVocabularyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.title.setText("Topics List");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        list = new ArrayList<>();

        binding.topics.setLayoutManager(new LinearLayoutManager(this));
        binding.topics.setHasFixedSize(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        Constants.showDialog();
        String name = Stash.getString(Constants.SELECT, Constants.URDU);
        Constants.databaseReference().child(name).child(Constants.VOCABULARY).child(Constants.TOPICS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Constants.dismissDialog();
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TopicsModel topicsModel = dataSnapshot.getValue(TopicsModel.class);
                        list.add(topicsModel);
                    }

                    if (list.size() > 0){
                        Collections.reverse(list);
                        binding.topics.setVisibility(View.VISIBLE);
                        binding.noLayout.setVisibility(View.GONE);
                    } else {
                        binding.topics.setVisibility(View.GONE);
                        binding.noLayout.setVisibility(View.VISIBLE);
                    }

                    ModelTopicsAdapter adapter = new ModelTopicsAdapter(VocabularyActivity.this, list);
                    binding.topics.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Constants.dismissDialog();
                Toast.makeText(VocabularyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}