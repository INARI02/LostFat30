package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.ui.interfaces.SectionItemClickListener;
import com.nhn.fitness.utils.ViewUtils;

public class SectionAdapter extends ListAdapter<SectionUser, SectionAdapter.ViewHolder> {

    private SectionItemClickListener listener;

    Context mContext;
    public SectionAdapter(@NonNull DiffUtil.ItemCallback<SectionUser> diffCallback, SectionItemClickListener listener, Context context) {
        super(diffCallback);
        this.listener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item_layout, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btnFavorite;
        private AppCompatImageView imgThumb;
        private AppCompatTextView txtTitle, txtSub;
        private AppCompatImageView ivLevel1,ivLevel2,ivLevel3;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_thumb);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtSub = itemView.findViewById(R.id.txt_sub);
            ivLevel1 = itemView.findViewById(R.id.ivLevel1);
            ivLevel2 = itemView.findViewById(R.id.ivLevel2);
            ivLevel3 = itemView.findViewById(R.id.ivLevel3);


            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnFavorite.setOnClickListener(this);
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void bind(SectionUser section) {
            txtTitle.setText(section.getData().getTitleDisplay());
            txtSub.setText(section.getData().getNumberWorkoutsString(txtSub.getContext()));
            // TODO check chuc nang checked
//            btnFavorite.setChecked(section.isFavorite(), false);
            int idLevel= section.getData().getLevel();

            if(idLevel==1){
                ivLevel1.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            }else if(idLevel==2){
                ivLevel1.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
                ivLevel2.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            }else if(idLevel==3){
                ivLevel1.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
                ivLevel2.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
                ivLevel3.setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            }

            ViewUtils.bindImage(imgThumb.getContext(), ViewUtils.getPathSection(section.getData().getThumb()), imgThumb);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_favorite) {
                SectionUser sectionUser = getItem(getAdapterPosition());
                sectionUser.reverseFavorite();
                SectionRepository.getInstance().updateSectionUser(sectionUser).subscribe();
            } else {
                listener.onSectionClick(view, getItem(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

}

