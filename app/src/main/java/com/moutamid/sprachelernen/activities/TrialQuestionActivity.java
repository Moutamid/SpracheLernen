package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.SubscriptionActivity;
import com.moutamid.sprachelernen.adapters.ChipsAdapter;
import com.moutamid.sprachelernen.adapters.OptionsAdapter;
import com.moutamid.sprachelernen.databinding.ActivityTrialQuestionBinding;
import com.moutamid.sprachelernen.listeners.ChipsClick;
import com.moutamid.sprachelernen.models.ExerciseModel;
import com.moutamid.sprachelernen.models.QuestionsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class TrialQuestionActivity extends AppCompatActivity {
    ActivityTrialQuestionBinding binding;
    ArrayList<ExerciseModel> list;
    ArrayList<String> sorting;
    ArrayList<String> fIB; // Fill in the blank
    int counter = 0, correct = 0;
    ChipsAdapter chipsAdapter;
    OptionsAdapter optionsAdapter;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrialQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        Constants.showDialog();

        binding.progressbar.close.setOnClickListener(v -> onBackPressed());

        binding.chipsRC.setHasFixedSize(false);
        binding.optionsRC.setHasFixedSize(false);
        binding.chipsRC.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.optionsRC.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        list = new ArrayList<>();
        sorting = new ArrayList<>();
        fIB = new ArrayList<>();

        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.TRIAL_QUESTIONS)
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        list.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ExerciseModel model = snapshot.getValue(ExerciseModel.class);
                            list.add(model);
                        }
                        Collections.shuffle(list);
                        setView(counter);
                    } else {
                        Toast.makeText(this, "Exercise not available", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.check.setOnClickListener(v -> {
            ExerciseModel model = list.get(counter);

            if (model.isMCQs()) {
                checkMCQs(model);
            } else if (model.isOrder()) {
                checkOrder(model);
            } else if (model.isFillBlank()) {
                if (model.getRightAnswer().equalsIgnoreCase(fIB.get(0))) {
                    correct += 1;
                }
            } else {
                checkRadio(model);
            }

            if (counter < list.size() - 1) {
                setView(counter + 1);
            } else {
                startActivity(new Intent(this, SubscriptionActivity.class));
                finish();
            }
        });
    }

    private void setView(int count) {
        ExerciseModel model = list.get(count);
        counter = count;
        binding.progressbar.track.setMax(list.size());
        binding.progressbar.track.setProgress(counter + 1);
        binding.progressbar.position.setText(counter + 1 + "/" + list.size());
        binding.isMultiple.setVisibility(View.GONE);
        binding.options.setVisibility(View.GONE);
        binding.otherOptions.setVisibility(View.GONE);
        binding.sortingLayout.setVisibility(View.GONE);
        if (model.isMCQs()) {
            binding.options.setVisibility(View.VISIBLE);
            binding.isMultiple.setVisibility(View.VISIBLE);
            binding.question.setText(model.getQuestion());
            binding.options.removeAllViews();
            for (String s : model.getOptions()) {
                View customRadioLayout = getLayoutInflater().inflate(R.layout.checkbox_button, null);
                MaterialCheckBox customRadio = customRadioLayout.findViewById(R.id.checkbox);
                customRadio.setText(s);
                binding.options.addView(customRadioLayout);
            }
        } else if (model.isFillBlank()) {
            binding.sortingLayout.setVisibility(View.VISIBLE);
            binding.otherOptions.setVisibility(View.VISIBLE);
            binding.question.setText(model.getQuestion());
            fIB.clear();
            chipsAdapter = new ChipsAdapter(this, fIB, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);

            optionsAdapter = new OptionsAdapter(this, model.getOptions(), optionsClick);
            binding.optionsRC.setAdapter(optionsAdapter);
        } else if (model.isOrder()) {
            binding.sortingLayout.setVisibility(View.VISIBLE);
            binding.otherOptions.setVisibility(View.VISIBLE);
            binding.question.setText(model.getQuestion());
            sorting.clear();
            chipsAdapter = new ChipsAdapter(this, sorting, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);

            optionsAdapter = new OptionsAdapter(this, model.getOptions(), optionsClick);
            binding.optionsRC.setAdapter(optionsAdapter);
        } else {
            binding.options.setVisibility(View.VISIBLE);
            binding.question.setText(model.getQuestion());
            binding.options.removeAllViews();

            radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            binding.options.addView(radioGroup);

            for (String s : model.getOptions()) {
                MaterialRadioButton button = new MaterialRadioButton(this);
                button.setId(View.generateViewId());
                button.setText(s);
                button.setTextSize(16f);
                button.setPadding(16, 10, 10, 10);
                button.setButtonTintList(ColorStateList.valueOf(getColor(R.color.greenDark)));
                radioGroup.addView(button);
            }

        }
    }

    public ChipsClick chipsClick = this::update;

    private void update(int position) {
        ExerciseModel model = list.get(counter);
        if (model.isOrder()) {
            sorting.remove(position);
            chipsAdapter = new ChipsAdapter(this, sorting, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);
        } else {
            fIB.clear();
            chipsAdapter = new ChipsAdapter(this, fIB, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);
        }
    }

    ChipsClick optionsClick = position -> {
        ExerciseModel model = list.get(counter);
        if (model.isOrder()) {
            sorting.add(model.getOptions().get(position));
            chipsAdapter = new ChipsAdapter(this, sorting, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);
        } else {
            fIB.clear();
            fIB.add(model.getOptions().get(position));
            chipsAdapter = new ChipsAdapter(this, fIB, chipsClick);
            binding.chipsRC.setAdapter(chipsAdapter);
        }
    };

    private void checkMCQs(ExerciseModel model) {
        String ans = model.getRightAnswer();
        ans.replace(", ", ",");
        String out = "";
        for (int i = 0; i < binding.options.getChildCount(); i++) {
            View view = binding.options.getChildAt(i);
            if (view instanceof MaterialCheckBox) {
                MaterialCheckBox checkBox = (MaterialCheckBox) view;
                String enteredText = checkBox.getText().toString();
                out += enteredText + ",";
            }
        }
        out = out.substring(0, out.length() - 1);
        if (out.equalsIgnoreCase(ans)) {
            correct += 1;
        }
    }

    private void checkRadio(ExerciseModel model) {
        try {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId != -1) {
                MaterialRadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {
                    String checkedText = checkedRadioButton.getText().toString();
                    if (checkedText.equalsIgnoreCase(model.getRightAnswer())) {
                        correct += 1;
                    }
                    Log.d("CheckedRadioButton", "Text: " + checkedText);
                }
            } else {
                Log.d("CheckedRadioButton", "No RadioButton is checked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkOrder(ExerciseModel model) {
        String ans = model.getRightAnswer();
        ans.replace(", ", ",");
        String[] out = ans.split(",");
        try {
            boolean check = false;
            if (out.length == sorting.size()) {
                for (int i = 0; i < out.length; i++) {
                    if (!out[i].equalsIgnoreCase(sorting.get(i))) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    correct += 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}