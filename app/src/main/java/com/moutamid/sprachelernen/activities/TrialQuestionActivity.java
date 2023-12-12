package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.SubscriptionActivity;
import com.moutamid.sprachelernen.databinding.ActivityTrialQuestionBinding;
import com.moutamid.sprachelernen.models.QuestionsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class TrialQuestionActivity extends AppCompatActivity {
    ActivityTrialQuestionBinding binding;
    ArrayList<QuestionsModel> list;
    int position = 0;
    int countCorrect = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrialQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = getQuestions();

        updateUI(position);

        binding.progressbar.close.setOnClickListener(v -> onBackPressed());

        binding.check.setOnClickListener(v -> {
            if (position < list.size()-1){
                position+=1;
                updateUI(position);
            } else {
                startActivity(new Intent(this, SubscriptionActivity.class));
                finish();
            }
        });

    }

    private void updateUI(int pos) {
        if (pos == list.size() -1){
            binding.check.setText("Check Answers");
        }
        binding.options.removeAllViews();

        int step = pos+1;

        binding.progressbar.track.setProgress(step, true);
        binding.progressbar.track.setMax(list.size());

        binding.progressbar.position.setText(step + "/" + list.size());
        QuestionsModel model = list.get(pos);
        binding.question.setText(model.getQuestion());

        if (model.isMCQs()) {
            binding.isMultiple.setVisibility(View.VISIBLE);

            for (String s : model.getAnswers()){
                MaterialCheckBox checkBox = new MaterialCheckBox(this);
                checkBox.setText(s);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                });
                binding.options.addView(checkBox);
            }

        } else {
            binding.isMultiple.setVisibility(View.GONE);
            for (String s : model.getAnswers()) {
                MaterialRadioButton radioButton = new MaterialRadioButton(this);
                radioButton.setText(s);
                binding.options.addView(radioButton);
            }
        }
    }

    private ArrayList<QuestionsModel> getQuestions() {
        ArrayList<QuestionsModel> list = new ArrayList<>();

        ArrayList<String> ans1 = new ArrayList<>();
        ans1.add("4");
        ans1.add("6");
        ans1.add("8");
        ans1.add("9");

        ArrayList<String> ans5 = new ArrayList<>();
        ans5.add("true");
        ans5.add("false");

        list.add(new QuestionsModel(UUID.randomUUID().toString(), "What is 2+2?", "4", ans1,false));
        list.add(new QuestionsModel(UUID.randomUUID().toString(), "What is 4+2?", "6", ans1,false));
        list.add(new QuestionsModel(UUID.randomUUID().toString(), "is 2 = 3", "false", ans5,false));

        ArrayList<String> ans3 = new ArrayList<>();
        ans3.add("English");
        ans3.add("Computer");
        ans3.add("Urdu");

        list.add(new QuestionsModel(UUID.randomUUID().toString(), "which one are languages", "82", ans3,true));

        list.add(new QuestionsModel(UUID.randomUUID().toString(), "What is 6+2?", "8", ans1,false));

        ArrayList<String> ans2 = new ArrayList<>();
        ans2.add("41");
        ans2.add("62");
        ans2.add("82");

        list.add(new QuestionsModel(UUID.randomUUID().toString(), "What is 80+2?", "82", ans2,false));
        Collections.shuffle(list);
        return list;
    }
}