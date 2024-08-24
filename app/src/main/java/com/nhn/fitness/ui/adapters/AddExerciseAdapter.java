package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import com.nhn.fitness.ui.activities.EditExerciseActivity;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;

public class AddExerciseAdapter extends RecyclerView.Adapter<AddExerciseAdapter.ViewHolder> {

    private Activity activity;
    private FragmentManager fragmentManager;
    private ArrayList<Workout> workouts;

    public AddExerciseAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Workout> list) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.workouts = list;
    }

    @NonNull
    @Override
    public AddExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item_add_layout, parent, false);
        return new AddExerciseAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AddExerciseAdapter.ViewHolder holder, int position) {
        holder.bind(workouts.get(position));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private AppCompatImageView imageView;
        private AppCompatTextView txtTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.img_thumb);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bind(Workout workoutUser) {
            ViewUtils.bindImage(imageView.getContext(), ViewUtils.getPathWorkout(workoutUser.getImageGender()), imageView);
            txtTitle.setText(workoutUser.getTitleDisplay());
        }

        @Override
        public void onClick(View view) {
            showEdit(getAdapterPosition());
        }
    }

    private void showEdit(int position) {
        Intent intent = new Intent(activity, EditExerciseActivity.class);
        intent.putExtra("com/nhn/fitness/data", workouts.get(position));
        activity.startActivityForResult(intent, 123);
    }
}
