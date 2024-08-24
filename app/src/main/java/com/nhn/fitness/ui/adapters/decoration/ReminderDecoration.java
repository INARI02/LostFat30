package com.nhn.fitness.ui.adapters.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.utils.ViewUtils;


public class ReminderDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    public ReminderDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = (int) ViewUtils.convertDpToPixel(100, context);
        }
    }
}
