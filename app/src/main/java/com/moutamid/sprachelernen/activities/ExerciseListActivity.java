package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.ExerciseListAdapter;
import com.moutamid.sprachelernen.databinding.ActivityExerciseListBinding;
import com.moutamid.sprachelernen.models.ExerciseListModel;
import com.moutamid.sprachelernen.models.ExerciseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExerciseListActivity extends AppCompatActivity {
    ActivityExerciseListBinding binding;
    ArrayList<ExerciseModel> list;
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        level = getIntent().getStringExtra(Constants.LEVEL);

        binding.toolbar.title.setText(level + " Exercises");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.exerixeRC.setLayoutManager(new LinearLayoutManager(this));
        binding.exerixeRC.setHasFixedSize(false);

        list = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        showData();
    }

    private void showData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.EXERCISE).child(level)
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        list.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ExerciseModel model = snapshot.getValue(ExerciseModel.class);
                            list.add(model);
                        }
                        updateView();
                    } else {
                        Toast.makeText(this, "Exercise not available", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateView() {
        Map<String, ArrayList<ExerciseModel>> groupedMap = new HashMap<>();
        for (ExerciseModel model : list) {
            groupedMap.computeIfAbsent(model.getExerciseName(), k -> new ArrayList<>()).add(model);
        }

        ArrayList<ExerciseListModel> otherModelList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<ExerciseModel>> entry : groupedMap.entrySet()) {
            String name = entry.getKey();
            ArrayList<ExerciseModel> modelsWithSameName = entry.getValue();
            otherModelList.add(new ExerciseListModel(name, modelsWithSameName));
        }
        ExerciseListAdapter adapter = new ExerciseListAdapter(this, otherModelList);
        binding.exerixeRC.setAdapter(adapter);
    }
}