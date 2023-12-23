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
import com.moutamid.sprachelernen.activities.ExerciseQuestionActivity;
import com.moutamid.sprachelernen.models.ExerciseListModel;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseVH> {
    Context context;
    ArrayList<ExerciseListModel> list;

    public ExerciseListAdapter(Context context, ArrayList<ExerciseListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExerciseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseVH(LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseVH holder, int position) {
        ExerciseListModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.getName());
        String s = model.getList().size() > 1 ? " Q\'s" : " Q";
        holder.count.setText(model.getList().size() + s);

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ExerciseQuestionActivity.class).putExtra(Constants.LEVEL, model.getList().get(0).getLevel()).putExtra(Constants.exercise, model.getName()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExerciseVH extends RecyclerView.ViewHolder{
        TextView name, count;
        public ExerciseVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            count = itemView.findViewById(R.id.count);
        }
    }

}
