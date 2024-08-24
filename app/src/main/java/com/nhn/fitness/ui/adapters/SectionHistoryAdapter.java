package com.nhn.fitness.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.SectionHistory;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SectionHistoryAdapter extends RecyclerView.Adapter<SectionHistoryAdapter.ViewHolder> {
    private ArrayList<SectionHistory> list;

    public SectionHistoryAdapter(ArrayList<SectionHistory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_history, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtTime, txtTimer, txtCalories;
        private CircleImageView imgImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtTimer = itemView.findViewById(R.id.txt_timer);
            txtCalories = itemView.findViewById(R.id.txt_calories);
            imgImage = itemView.findViewById(R.id.img_thumb);
        }

        void bind(SectionHistory sectionHistory) {
            txtTitle.setText(sectionHistory.getTitle());
            txtTime.setText(sectionHistory.getTimeFormat());
            txtTimer.setText(sectionHistory.getTimerString());
            txtCalories.setText(String.format(Locale.US, "%.2f %s", sectionHistory.getCalories(), itemView.getResources().getString(R.string.calories)));
            ViewUtils.bindImage(imgImage.getContext(), ViewUtils.getPathSection(sectionHistory.getThumb()), imgImage);
        }
    }
}
