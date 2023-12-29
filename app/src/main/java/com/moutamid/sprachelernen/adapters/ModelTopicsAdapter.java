package com.moutamid.sprachelernen.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.activities.VocabularyContentActivity;
import com.moutamid.sprachelernen.models.TopicsModel;

import java.util.ArrayList;

public class ModelTopicsAdapter extends RecyclerView.Adapter<ModelTopicsAdapter.ModelTopicsVH> {

    Context context;
    ArrayList<TopicsModel> list;

    public ModelTopicsAdapter(Context context, ArrayList<TopicsModel> list) {
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
        TopicsModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.getTopicName());
        holder.content.setText(model.getContentType());

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, VocabularyContentActivity.class).putExtra(Constants.TOPICS, model.getID()));
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
