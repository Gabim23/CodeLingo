package com.example.codelingo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {

    private String[] levels;
    private OnLevelClickListener listener;
    private Context context;

    public LevelsAdapter(Context context, String[] levels, OnLevelClickListener listener) {
        this.context = context;
        this.levels = levels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        String level = levels[position];
        holder.tvLevel.setText(level);
        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(16f); // Esquinas redondeadas
        if (level.contains("Bloqueado")) {
            background.setColor(ContextCompat.getColor(context, R.color.grey));
        } else {
            background.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        holder.tvLevel.setBackground(background);
        holder.itemView.setOnClickListener(v -> {
            if (!level.contains("Bloqueado")) {
                listener.onLevelClick(position);
            } else {
                Toast.makeText(context, "Este nivel está bloqueado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return levels.length;
    }

    static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView tvLevel;
        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }
    }
}