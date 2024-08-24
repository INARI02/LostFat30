package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.ui.dialogs.ReplaceWorkoutDialog;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;

public class WorkoutReplaceAdapter extends RecyclerView.Adapter<WorkoutReplaceAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private ArrayList<Workout> workoutUsers;

    public WorkoutReplaceAdapter(FragmentManager fragmentManager, ArrayList<Workout> list) {
        this.fragmentManager = fragmentManager;
        this.workoutUsers = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item_replace_layout, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private AppCompatImageView imageView;
        private AppCompatTextView txtTitle, txtNumber;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.img_thumb);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtNumber = itemView.findViewById(R.id.txt_number);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(Workout workoutUser) {
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathWorkout(workoutUser.getImageGender()), imageView);
            txtTitle.setText(workoutUser.getTitleDisplay());
            txtNumber.setText(workoutUser.getTimeCountString());
        }

        @Override
        public void onClick(View view) {
            showEdit(getAdapterPosition());
        }
    }

    private void showEdit(int position) {
        if (!fragmentManager.isDestroyed()) {
            new ReplaceWorkoutDialog(workoutUsers.get(position)).show(fragmentManager, null);
        }
    }
}

