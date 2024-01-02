package com.moutamid.sprachelernen.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.adapters.VocabularyAdapter;
import com.moutamid.sprachelernen.databinding.ActivityVocabularyContentBinding;
import com.moutamid.sprachelernen.models.VocabularyModel;

import java.util.ArrayList;

public class VocabularyContentActivity extends BaseSecureActivity {
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

        binding.toolbar.title.setText("Vocabulary");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.topics.setLayoutManager(new GridLayoutManager(this, 2));
        binding.topics.setHasFixedSize(false);

        binding.current.setText(Constants.getLanguage());

        list = new ArrayList<>();

        binding.current.setOnClickListener(v -> {
            if (adapters != null) {
                adapters.releaseMediaPlayer();
            }
            binding.indicator1.setVisibility(View.VISIBLE);
            binding.indicator2.setVisibility(View.GONE);
            adapters = new VocabularyAdapter(VocabularyContentActivity.this, list, false);
            binding.topics.setAdapter(adapters);
        });
        binding.german.setOnClickListener(v -> {
            if (adapters != null) {
                adapters.releaseMediaPlayer();
            }
            binding.indicator1.setVisibility(View.GONE);
            binding.indicator2.setVisibility(View.VISIBLE);
            adapters = new VocabularyAdapter(VocabularyContentActivity.this, list, true);
            binding.topics.setAdapter(adapters);
        });

        getContent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapters != null) {
            adapters.releaseMediaPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapters != null) {
            adapters.releaseMediaPlayer();
        }
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

                            adapters = new VocabularyAdapter(VocabularyContentActivity.this, list, false);
                            binding.topics.setAdapter(adapters);
                        } else {
                            Toast.makeText(VocabularyContentActivity.this, "Vocabulary Not Found", Toast.LENGTH_SHORT).show();
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