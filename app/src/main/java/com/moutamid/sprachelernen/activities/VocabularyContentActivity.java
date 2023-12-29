package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.VocabularyAdapter;
import com.moutamid.sprachelernen.databinding.ActivityVocabularyContentBinding;
import com.moutamid.sprachelernen.models.VocabularyModel;

import java.util.ArrayList;
import java.util.Collections;

public class VocabularyContentActivity extends AppCompatActivity {
    ActivityVocabularyContentBinding binding;
    String ID;
    ArrayList<VocabularyModel> list;
    VocabularyAdapter adapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVocabularyContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra(Constants.TOPICS);
        Constants.initDialog(this);

        binding.topics.setLayoutManager(new GridLayoutManager(this, 2));
        binding.topics.setHasFixedSize(false);

        getContent();

    }

    private void getContent() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.VOCABULARY).child(Constants.CONTENT).child(ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Constants.dismissDialog();
                        if (dataSnapshot.exists()) {
                            list.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                VocabularyModel model = snapshot.getValue(VocabularyModel.class);
                                list.add(model);
                            }

                            if (list.size() > 0){
                                Toast.makeText(VocabularyContentActivity.this, "Vocabulary Not Found", Toast.LENGTH_SHORT).show();
                            }

                            adapters = new VocabularyAdapter(VocabularyContentActivity.this, list, false);
                            binding.topics.setAdapter(adapters);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Constants.dismissDialog();
                        Toast.makeText(VocabularyContentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}