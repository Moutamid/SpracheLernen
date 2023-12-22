package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.OptionsListAdapter;
import com.moutamid.sprachelernen.databinding.ActivityTopicsBinding;
import com.moutamid.sprachelernen.models.ContentModel;
import com.moutamid.sprachelernen.models.TopicsModel;

public class TopicsActivity extends AppCompatActivity {
    ActivityTopicsBinding binding;
    TopicsModel topic;
    ContentModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topic = (TopicsModel) Stash.getObject(Constants.TOPICS, TopicsModel.class);

        binding.toolbar.title.setText(topic.getTopicName());
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        Constants.initDialog(this);
        Constants.showDialog();

        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.CONTENT).child(topic.getContentType()).child(topic.getID())
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        model = dataSnapshot.getValue(ContentModel.class);
                        updateViews();
                    } else {
                        Toast.makeText(this, "Content is not available", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateViews() {
        binding.heading.setText(model.getHeading());
        binding.note.setText(model.getNote());

        if (model.isHasOptions()) {
            binding.optionsRC.setVisibility(View.VISIBLE);
            binding.optionsRC.setHasFixedSize(false);
            binding.optionsRC.setLayoutManager(new LinearLayoutManager(this));
            OptionsListAdapter adapter = new OptionsListAdapter(this, model.getOptions());
            binding.optionsRC.setAdapter(adapter);
        }
        if (model.isHaveTable()) {
            binding.table.setVisibility(View.VISIBLE);
            for (int i=0; i<model.getRows().size(); i++) {
                String s = model.getRows().get(i);
                s = s.replace(", ", ",");
                String[] columns = s.split(",");
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (i%2 == 0) {
                    layout.setBackgroundColor(getColor(R.color.grey));
                } else {
                    layout.setBackgroundColor(getColor(R.color.greenLight));
                }
                layout.setPadding(12,12,12,12);
                binding.tableView.addView(layout);
                for (String col : columns) {
                    TextView textView = new TextView(this);
                    textView.setText(col);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(16);
                    textView.setPadding(12,12,12,12);
                    layout.addView(textView);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.TOPICS);
    }
}