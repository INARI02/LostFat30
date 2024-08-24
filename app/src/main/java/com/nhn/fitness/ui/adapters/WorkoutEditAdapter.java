package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperAdapter;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperViewHolder;
import com.nhn.fitness.ui.adapters.helper.OnStartDragListener;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;

public class WorkoutEditAdapter extends RecyclerView.Adapter<WorkoutEditAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private final ArrayList<WorkoutUser> workoutUsers;
    private final OnStartDragListener dragStartListener;
    private final ItemTouchHelperAdapter itemTouchHelperAdapter;

    public WorkoutEditAdapter(ArrayList<WorkoutUser> list,
                              OnStartDragListener startDragListener,
                              ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.workoutUsers = list;
        this.dragStartListener = startDragListener;
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item_layout, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(workoutUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutUsers.size();
    }

    @Override
    public void onItemDismiss(int position) {
        itemTouchHelperAdapter.onItemDismiss(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return itemTouchHelperAdapter.onItemMove(fromPosition, toPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {
        private AppCompatImageView imageView;
        private AppCompatTextView txtTitle, txtNumber;
        private View btnMove, btnChange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_thumb);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtNumber = itemView.findViewById(R.id.txt_number);
            btnMove = itemView.findViewById(R.id.btn_move);
            btnChange = itemView.findViewById(R.id.btn_change);
            btnMove.setVisibility(View.VISIBLE);
            btnChange.setVisibility(View.VISIBLE);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(WorkoutUser workoutUser) {
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
            txtTitle.setText(workoutUser.getData().getTitleDisplay());
            txtNumber.setText("00:30");

            btnMove.setOnTouchListener((v, event) -> {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onStartDrag(ViewHolder.this);
                }
                return false;
            });
        }

        @Override
        public void onItemSelected() {
//            if (isEditMode) {
//                itemView.setBackgroundColor(Color.LTGRAY);
//            }
        }

        @Override
        public void onItemClear() {
//            if (isEditMode) {
//                itemView.setBackgroundColor(0);
//            }
        }
    }
}

