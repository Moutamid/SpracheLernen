package com.moutamid.sprachelernen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.listeners.ChipsClick;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ChipsVH> {
    Context context;
    ArrayList<String> list;
    ChipsClick chipsClick;

    public OptionsAdapter(Context context, ArrayList<String> list, ChipsClick chipsClick) {
        this.context = context;
        this.list = list;
        this.chipsClick = chipsClick;
    }

    @NonNull
    @Override
    public ChipsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChipsVH(LayoutInflater.from(context).inflate(R.layout.options, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsVH holder, int position) {
        String s = list.get(holder.getAdapterPosition());
        holder.name.setText(s);
        holder.itemView.setOnClickListener(v -> chipsClick.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChipsVH extends RecyclerView.ViewHolder{
        TextView name;
        public ChipsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }


}
