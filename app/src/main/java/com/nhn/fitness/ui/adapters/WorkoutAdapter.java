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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperAdapter;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperViewHolder;
import com.nhn.fitness.ui.adapters.helper.OnStartDragListener;
import com.nhn.fitness.ui.dialogs.WorkoutInfoDialog;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private FragmentManager fragmentManager;
    private ArrayList<WorkoutUser> workoutUsers;
    private final OnStartDragListener dragStartListener;
    private final ItemTouchHelperAdapter itemTouchHelperAdapter;
    private final boolean isEditMode;
    private WorkoutChangeClickListener listener;

    public WorkoutAdapter(FragmentManager fragmentManager, ArrayList<WorkoutUser> list) {
        this.fragmentManager = fragmentManager;
        this.workoutUsers = list;
        this.isEditMode = false;
        this.dragStartListener = null;
        this.itemTouchHelperAdapter = null;
    }

    public WorkoutAdapter(FragmentManager fragmentManager,
                          ArrayList<WorkoutUser> list,
                          OnStartDragListener startDragListener,
                          ItemTouchHelperAdapter itemTouchHelperAdapter,
                          WorkoutChangeClickListener listener) {
        this.fragmentManager = fragmentManager;
        this.workoutUsers = list;
        this.isEditMode = true;
        this.dragStartListener = startDragListener;
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
        this.listener = listener;
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
        if (isEditMode && itemTouchHelperAdapter != null) {
            itemTouchHelperAdapter.onItemDismiss(position);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isEditMode && itemTouchHelperAdapter != null) {
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
            btnChange = itemView.findViewById(R.id.btn_change);
            btnChange.setOnClickListener(this);

            if (isEditMode) {
                btnMove.setVisibility(View.VISIBLE);
                btnChange.setVisibility(View.VISIBLE);
            } else {
                btnMove.setVisibility(View.GONE);
                btnChange.setVisibility(View.GONE);
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(WorkoutUser workoutUser) {
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
            txtTitle.setText(workoutUser.getData().getTitleDisplay());
            txtNumber.setText(workoutUser.getTimeCountString());

            if (isEditMode) {
                btnMove.setOnTouchListener((v, event) -> {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragStartListener.onStartDrag(ViewHolder.this);
                    }
                    return false;
                });
            }
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
                if (listener != null) {
                    listener.onChange(getAdapterPosition(), workoutUsers.get(getAdapterPosition()));
                }
            } else {
                showEdit(getAdapterPosition());
            }
        }
    }

    private void showEdit(int position) {
        if (!fragmentManager.isDestroyed()) {
            new WorkoutInfoDialog(workoutUsers, position).show(fragmentManager, null);
        }
    }

    public interface WorkoutChangeClickListener {
        void onChange(int pos, WorkoutUser workoutUser);
    }
}
