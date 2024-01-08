package com.moutamid.sprachelernen.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityWritingContentBinding;
import com.moutamid.sprachelernen.models.WritingModel;

import java.io.IOException;

public class WritingContentActivity extends AppCompatActivity {
    ActivityWritingContentBinding binding;
    WritingModel model;
    String ID;
    boolean isGerman = false;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWritingContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ID = getIntent().getStringExtra(Constants.ID);
        Constants.initDialog(this);

        mediaPlayer = new MediaPlayer();

        binding.toolbar.title.setText("Writing");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.current.setOnClickListener(v -> {
            stopPlayer();
            binding.indicator1.setVisibility(View.VISIBLE);
            binding.indicator2.setVisibility(View.GONE);
            updateUrdu();
        });
        binding.german.setOnClickListener(v -> {
            stopPlayer();
            binding.indicator1.setVisibility(View.GONE);
            binding.indicator2.setVisibility(View.VISIBLE);
            updateGerman();
        });

        binding.play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                stopPlayer();
                Glide.with(this).load(R.drawable.play).into(binding.playIcon);
            } else {
                Glide.with(this).load(R.drawable.round_pause_24).into(binding.playIcon);
                playAudio();
            }
        });

        getData();
    }

    private void playAudio() {
        String audioUrl = isGerman ? model.getGermanAudio() : model.getAudio();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                binding.progress.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                updateProgress();
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                    Glide.with(WritingContentActivity.this).load(R.drawable.play).into(binding.playIcon);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgress() {
        if (mediaPlayer != null) {
            binding.progress.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(() -> updateProgress(), 100);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayer();
    }

    private void getData() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.WRITING).child(ID)
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        model = dataSnapshot.getValue(WritingModel.class);
                        updateUrdu();
                    } else Toast.makeText(this, "Content Not Found", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUrdu() {
        isGerman = false;
        binding.topic.setText(model.getTopic());
        binding.letter.setText(model.getLetter());
        Glide.with(this).load(R.drawable.pakistan).into(binding.flag);
    }

    private void updateGerman() {
        isGerman = true;
        binding.topic.setText(model.getGermanTopic());
        binding.letter.setText(model.getGermanLetter());
        Glide.with(this).load(R.drawable.germany).into(binding.flag);
    }

}