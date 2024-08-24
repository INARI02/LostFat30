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

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<WorkoutUser> workoutUsers;
    private final OnStartDragListener dragStartListener;
    private final ItemTouchHelperAdapter itemTouchHelperAdapter;
    private ExerciseAdapterListener listener;

    public ExerciseAdapter(ArrayList<WorkoutUser> list,
                           OnStartDragListener startDragListener,
                           ItemTouchHelperAdapter itemTouchHelperAdapter,
                           ExerciseAdapterListener listener) {
        this.workoutUsers = list;
        this.dragStartListener = startDragListener;
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item_layout, parent, false);
        return new ExerciseAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        holder.bind(workoutUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutUsers.size();
    }

    @Override
    public void onItemDismiss(int position) {
        if (itemTouchHelperAdapter != null) {
            itemTouchHelperAdapter.onItemDismiss(position);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (itemTouchHelperAdapter != null) {
            return itemTouchHelperAdapter.onItemMove(fromPosition, toPosition);
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener {
        private AppCompatImageView imageView;
        private AppCompatTextView txtTitle, txtNumber;
        private View btnMove, btnChange;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.img_thumb);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtNumber = itemView.findViewById(R.id.txt_number);
            btnMove = itemView.findViewById(R.id.btn_move);
            btnChange = itemView.findViewById(R.id.btn_remove);
            btnChange.setOnClickListener(this);

            btnMove.setVisibility(View.VISIBLE);
            btnChange.setVisibility(View.VISIBLE);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(WorkoutUser workoutUser) {
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
            txtTitle.setText(workoutUser.getData().getTitleDisplay());
            txtNumber.setText(workoutUser.getTimeCountString());

            btnMove.setOnTouchListener((v, event) -> {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onStartDrag(ExerciseAdapter.ViewHolder.this);
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

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_remove) {
                listener.onRemove(getAdapterPosition());
            } else {
                listener.onEdit(getAdapterPosition());
            }
        }
    }

    public interface ExerciseAdapterListener {
        void onRemove(int pos);

        void onEdit(int pos);
    }

}