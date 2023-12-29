package com.moutamid.sprachelernen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.adapters.OptionsListAdapter;
import com.moutamid.sprachelernen.databinding.ActivityTopicsBinding;
import com.moutamid.sprachelernen.models.ContentModel;
import com.moutamid.sprachelernen.models.TopicsModel;

public class TopicsActivity extends BaseSecureActivity {
    ActivityTopicsBinding binding;
    TopicsModel topic;
    ContentModel model;
    private static final String TAG = "TopicsActivity";
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
            TableLayout tableLayout = new TableLayout(this);
            tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            for (int i=0; i<model.getRows().size(); i++) {
                String s = model.getRows().get(i);
                s = s.replace(", ", ",");
                String[] columns = s.split(",");
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                for (String col : columns) {
                    TextView textView = new TextView(this);
                    textView.setText(col);
                    textView.setLayoutParams(new TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                    textView.setGravity(Gravity.CENTER);
                    int size = i==0 ? 24 : 16;
                    textView.setTextSize(size);
                    textView.setPadding(21,21,21,21);
                    tableRow.addView(textView);
                }
                if (i%2==0) {
                    tableRow.setBackgroundColor(getColor(R.color.grey));
                } else {
                    tableRow.setBackgroundColor(getColor(R.color.greenLight));
                }
                tableLayout.addView(tableRow);
                Log.d(TAG, "updateViews: tableLayout");
            }
            binding.tableView.addView(tableLayout);
            Log.d(TAG, "updateViews: added");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stash.clear(Constants.TOPICS);
    }
}