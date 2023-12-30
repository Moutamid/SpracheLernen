package com.moutamid.sprachelernen.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.models.VocabularyModel;

import java.io.IOException;
import java.util.ArrayList;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyVH> {

    Context context;
    ArrayList<VocabularyModel> list;
    boolean isGerman;
    MediaPlayer mediaPlayer;
    int playingPosition = -1;
    private Handler handler = new Handler();

    public VocabularyAdapter(Context context, ArrayList<VocabularyModel> list, boolean isGerman) {
        this.context = context;
        this.list = list;
        this.isGerman = isGerman;
        this.mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public VocabularyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VocabularyVH(LayoutInflater.from(context).inflate(R.layout.vocabulary_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyVH holder, int position) {
        VocabularyModel model = list.get(holder.getAdapterPosition());
        Glide.with(context).load(model.getImage()).into(holder.imageView);
        String name = isGerman ? model.getNameGerman() : model.getName();
        holder.name.setText(name);

        holder.play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                if (playingPosition == position) {
                    playingPosition = -1;
                    return;
                }
//                holder.playIcon.setImageResource(R.drawable.round_play_arrow_24);
            }
//            holder.playIcon.setImageResource(R.drawable.round_pause_24);
            playingPosition = position;
            playAudio(model.getAudio(), holder.progress);
        });

    }

    private void playAudio(String audioUrl, final LinearProgressIndicator progress) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                progress.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                updateProgress(progress);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgress(final LinearProgressIndicator progress) {
        if (mediaPlayer != null) {
            progress.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(() -> updateProgress(progress), 100);
            } else {
                // Reset playing position when playback completes
                playingPosition = -1;
            }
        }
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VocabularyVH extends RecyclerView.ViewHolder {
        TextView name;
        MaterialCardView play;
        LinearProgressIndicator progress;
        ImageView imageView;
        public VocabularyVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            play = itemView.findViewById(R.id.play);
            progress = itemView.findViewById(R.id.progress);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
