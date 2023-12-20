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
import com.moutamid.sprachelernen.activities.TopicsActivity;
import com.moutamid.sprachelernen.models.TopicsModel;

import java.util.ArrayList;

public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.TopicVH> {
    Context context;
    ArrayList<TopicsModel> list;

    public TopicsListAdapter(Context context, ArrayList<TopicsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TopicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicVH(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicVH holder, int position) {
        TopicsModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.getTopicName());
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, TopicsActivity.class).putExtra(Constants.TOPIC_ID, model.getID())) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TopicVH extends RecyclerView.ViewHolder {
        TextView name;
        public TopicVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
        }
    }

}
