package com.nhn.fitness.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.TipsArticle;
import com.nhn.fitness.ui.activities.TipsDetailActivity;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {
    List<TipsArticle> list;

    public TipsAdapter() {
        list = new ArrayList<>();
    }

    public void setList(List<TipsArticle> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips_layout, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtContent, txtTitle;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtContent = itemView.findViewById(R.id.txt_content);
            imageView = itemView.findViewById(R.id.img_thumb);
        }

        void bind(TipsArticle data) {
            txtTitle.setText(data.getTitle());
            txtContent.setText(data.getContent());
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathSection(data.getImage()), imageView);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TipsDetailActivity.class);
            intent.putExtra("com/nhn/fitness/data", list.get(getAdapterPosition()));
            view.getContext().startActivity(intent);
        }
    }
}
