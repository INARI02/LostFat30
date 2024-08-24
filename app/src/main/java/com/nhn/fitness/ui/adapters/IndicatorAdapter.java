package com.nhn.fitness.ui.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {

    private int size = 0;
    private int pos = 0;

    public IndicatorAdapter(int count) {
        this.size = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_recycleview, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (pos >= position) {
            GradientDrawable gradientDrawable = (GradientDrawable) holder.viewIndicator.getBackground();
            gradientDrawable.setColor(holder.itemView.getContext().getResources().getColor(R.color.blueLight));
        } else {
            GradientDrawable gradientDrawable = (GradientDrawable) holder.viewIndicator.getBackground();
            gradientDrawable.setColor(holder.itemView.getContext().getResources().getColor(R.color.accentBlue));
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View viewIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            viewIndicator = itemView.findViewById(R.id.viewIndicator);
        }
    }
}
