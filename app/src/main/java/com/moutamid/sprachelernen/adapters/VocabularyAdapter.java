package com.moutamid.sprachelernen.adapters;

import android.content.Context;
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

import java.util.ArrayList;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyVH> {

    Context context;
    ArrayList<VocabularyModel> list;
    boolean isGerman;

    public VocabularyAdapter(Context context, ArrayList<VocabularyModel> list, boolean isGerman) {
        this.context = context;
        this.list = list;
        this.isGerman = isGerman;
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
