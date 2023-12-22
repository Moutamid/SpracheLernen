package com.moutamid.sprachelernen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.TopicsActivity;
import com.moutamid.sprachelernen.models.TopicsModel;

import java.util.ArrayList;

public class OptionsListAdapter extends RecyclerView.Adapter<OptionsListAdapter.TopicVH> {
    Context context;
    ArrayList<String> list;

    public OptionsListAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TopicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicVH(LayoutInflater.from(context).inflate(R.layout.option_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicVH holder, int position) {
        String model = list.get(holder.getAdapterPosition());
        holder.name.setText(model);
        holder.counter.setText(""+(holder.getAdapterPosition()+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TopicVH extends RecyclerView.ViewHolder {
        TextView name;
        TextView counter;
        public TopicVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
            counter = itemView.findViewById(R.id.counter);
        }
    }

}
