package com.moutamid.sprachelernen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.ChipsAdapter;
import com.moutamid.sprachelernen.adapters.OptionsAdapter;
import com.moutamid.sprachelernen.databinding.ActivityExerciseQuestionBinding;
import com.moutamid.sprachelernen.listeners.ChipsClick;
import com.moutamid.sprachelernen.models.ExerciseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ExerciseQuestionActivity extends BaseSecureActivity implements TextToSpeech.OnInitListener {
    ActivityExerciseQuestionBinding binding;
    ArrayList<ExerciseModel> list;
    ArrayList<String> sorting;
    ArrayList<String> fIB; // Fill in the blank
    int counter = 0, correct = 0;
    TextToSpeech textToSpeech;
    ChipsAdapter chipsAdapter;
    OptionsAdapter optionsAdapter;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        Constants.showDialog();

        textToSpeech = new TextToSpeech(this, this);

        String level = getIntent().getStringExtra(Constants.LEVEL);
        String exercise = getIntent().getStringExtra(Constants.exercise);

        binding.progressbar.close.setOnClickListener(v -> onBackPressed());

        binding.chipsRC.setHasFixedSize(false);
        binding.optionsRC.setHasFixedSize(false);
        binding.chipsRC.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.optionsRC.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        list = new ArrayList<>();
        sorting = new ArrayList<>();
        fIB = new ArrayList<>();

        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.EXERCISE).child(level)
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        list.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ExerciseModel model = snapshot.getValue(ExerciseModel.class);
                            if (exercise.equals(model.getExerciseName()))
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

        binding.play.setOnClickListener(v -> {
            ExerciseModel model = list.get(counter);
            textToSpeech.speak(model.getQuestion(), TextToSpeech.QUEUE_FLUSH, null, null);
        });

        binding.check.setOnClickListener(v -> {
            ExerciseModel model = list.get(counter);

            if (model.isMCQs()) {
                checkMCQs(model);
            } else if (model.isOrder()) {
                checkOrder(model);
            } else if (model.isFillBlank()) {
                checkFillinBlank(model);
            } else {
                checkRadio(model);
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
        ans = ans.replace(", ", ",");
        String out = "";
        boolean check = false;
        for (int i = 0; i < binding.options.getChildCount(); i++) {
            View view = binding.options.getChildAt(i);
            if (view instanceof MaterialCheckBox) {
                MaterialCheckBox checkBox = (MaterialCheckBox) view;
                if (checkBox.isChecked()) {
                    String enteredText = checkBox.getText().toString();
                    out += enteredText + ",";
                }
            }
        }
        Log.d("checkMCQs", "checkMCQs: " + out);
        out = out.substring(0, out.length() - 1);
        Log.d("checkMCQs", "checkMCQs: " + out);
        if (out.equalsIgnoreCase(ans)) {
            correct += 1;
            check = true;
        }
        showDialog(check);
    }

    private void checkFillinBlank(ExerciseModel model) {
        boolean check = false;
        if (model.getRightAnswer().equalsIgnoreCase(fIB.get(0))) {
            correct += 1;
            check = true;
        }
        showDialog(check);
    }


    private void showDialog(boolean check) {
        int view = check ? R.layout.result_correct : R.layout.result_incorrect;
        ExerciseModel model = list.get(counter);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        TextView correctAnswer = dialog.findViewById(R.id.correctAnswer);
        TextView explanation = dialog.findViewById(R.id.explanation);
        MaterialButton next = dialog.findViewById(R.id.next);

        correctAnswer.setText(model.getRightAnswer());
        explanation.setText(model.getExplanation());

        next.setOnClickListener(v -> {
            dialog.dismiss();
            if (counter < list.size() - 1) {
                setView(counter + 1);
            } else {
                startActivity(new Intent(this, CongratulationActivity.class).putExtra("correct", correct).putExtra("total", list.size()));
                finish();
            }
        });

    }

    private void checkRadio(ExerciseModel model) {
        boolean check = false;
        try {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId != -1) {
                MaterialRadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {
                    String checkedText = checkedRadioButton.getText().toString();
                    if (checkedText.equalsIgnoreCase(model.getRightAnswer())) {
                        correct += 1;
                        check = true;
                    }
                    Log.d("CheckedRadioButton", "Text: " + checkedText);
                }
            } else {
                Log.d("CheckedRadioButton", "No RadioButton is checked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showDialog(check);
    }

    private void checkOrder(ExerciseModel model) {
        boolean result = false;
        String ans = model.getRightAnswer();
        ans = ans.replace(", ", ",");
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
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showDialog(result);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                textToSpeech.setLanguage(Locale.UK);
            }
        }
    }

    @Override
    protected void onDestroy() {
        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }
}