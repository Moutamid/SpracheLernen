package com.moutamid.sprachelernen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.VocabularyContentActivity;
import com.moutamid.sprachelernen.activities.WritingContentActivity;
import com.moutamid.sprachelernen.models.TopicsModel;
import com.moutamid.sprachelernen.models.WritingModel;

import java.util.ArrayList;

public class WritingAdapter extends RecyclerView.Adapter<WritingAdapter.ModelTopicsVH> {

    Context context;
    ArrayList<WritingModel> list;

    public WritingAdapter(Context context, ArrayList<WritingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ModelTopicsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelTopicsVH(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelTopicsVH holder, int position) {
        WritingModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.getTopic());
        holder.content.setText("Writing");

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, WritingContentActivity.class).putExtra(Constants.ID, model.getId()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ModelTopicsVH extends RecyclerView.ViewHolder {
        TextView name;
        TextView content;

        public ModelTopicsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
            content = itemView.findViewById(R.id.content);
        }
    }

}
