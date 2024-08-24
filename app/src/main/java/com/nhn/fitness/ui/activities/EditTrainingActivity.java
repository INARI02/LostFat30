package com.nhn.fitness.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Section;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.data.repositories.WorkoutRepository;
import com.nhn.fitness.ui.adapters.ExerciseAdapter;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperAdapter;
import com.nhn.fitness.ui.adapters.helper.OnStartDragListener;
import com.nhn.fitness.ui.adapters.helper.SimpleItemTouchHelperCallback;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class EditTrainingActivity extends BaseActivity implements ExerciseAdapter.ExerciseAdapterListener,
        OnStartDragListener, ItemTouchHelperAdapter {

    ArrayList<WorkoutUser> workoutUsers = new ArrayList<>();
    RecyclerView recyclerView;
    ExerciseAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    SectionUser sectionUser;
    int posEdit = -1;
    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_training);
        setStatusBarColor(getResources().getColor(R.color.white));

        initData();
        initViews();
        initEvents();
        initObservers();
    }

    private void initData() {
        Object data = getIntent().getParcelableExtra("com/nhn/fitness/data");
        if (data instanceof WorkoutUser) {
            // New
            editMode = false;
            workoutUsers.add((WorkoutUser) data);
            sectionUser = new SectionUser(Utils.randomString(5));
            sectionUser.setTraining(true);
        } else {
            editMode = true;
            sectionUser = (SectionUser) data;
            // Load list
            loadWorkouts();
        }
    }

    private void loadWorkouts() {
        addDisposable(WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getWorkoutsId())
                .subscribe(response -> {
                    workoutUsers.clear();
                    workoutUsers.addAll(response);
                    adapter.notifyDataSetChanged();
                }));
    }

    private void initObservers() {

    }

    private void initEvents() {
        findViewById(R.id.btn_add).setOnClickListener(view -> {
            Intent intent = new Intent(EditTrainingActivity.this, AddExerciseActivity.class);
            intent.putExtra("com/nhn/fitness/data", false);
            startActivityForResult(intent, 122);
        });
        findViewById(R.id.btn_save).setOnClickListener(view -> {
            save();
        });
    }

    private void save() {
        if (editMode) {
            ArrayList<String> workoutIds = new ArrayList<>();
            ArrayList<String> workoutUserIds = new ArrayList<>();
            for (WorkoutUser workoutUser : workoutUsers) {
                workoutIds.add(workoutUser.getData().getId());
                workoutUserIds.add(workoutUser.getWorkoutUserId());
            }
            sectionUser.setWorkoutsId(workoutUserIds);
            addDisposable(
                    WorkoutRepository.getInstance().insert(workoutUsers)
                            .subscribe(() -> {
                                addDisposable(SectionRepository.getInstance()
                                        .updateSectionUser(sectionUser)
                                        .subscribe(this::onBackPressed, Throwable::printStackTrace));
                            })
            );
        } else {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.enter_name_training);
            AppCompatEditText editText = dialog.findViewById(R.id.edt_name);
            TextView btDialogAddName = (TextView) dialog.findViewById(R.id.tv_dialog_add_name);
            btDialogAddName.setOnClickListener(view1 -> {
                String txtValue = editText.getText().toString();
                if (!TextUtils.isEmpty(txtValue)) {
                    newTraining(editText.getText().toString());
                    dialog.dismiss();
                }else {
                    Toast.makeText(this, getString(R.string.required_missing), Toast.LENGTH_LONG).show();
                }
            });
            TextView btDialogCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
            btDialogCancel.setOnClickListener(view1 -> {
                   dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void newTraining(String name) {
        ArrayList<String> workoutIds = new ArrayList<>();
        ArrayList<String> workoutUserIds = new ArrayList<>();
        for (WorkoutUser workoutUser : workoutUsers) {
            workoutIds.add(workoutUser.getData().getId());
            workoutUserIds.add(workoutUser.getId());
        }
        Section section = new Section();
        section.setId(Utils.randomString(5));
        section.setTitle(name);
        section.setDescription("My Training");
        section.setThumb("absworkout1");
        section.setThumbFemale("absworkout1");
        section.setWorkoutsId(workoutIds);
        sectionUser = new SectionUser(section.getId());
        sectionUser.setTraining(true);
        sectionUser.setWorkoutsId(workoutUserIds);
        addDisposable(SectionRepository.getInstance().insert(section)
                .subscribe(() -> {
                    Log.e("status", "insert section");
                    addDisposable(SectionRepository.getInstance().insert(sectionUser)
                            .subscribe(() -> {
                                Log.e("status", "insert section user");
                                onBackPressed();
                            })
                    );
                })
        );
    }

    private void initViews() {
        TextView tvTitleNewTraining = findViewById(R.id.txt_title_new_training);
        findViewById(R.id.iv_back_new_training).setOnClickListener(view -> {
            onBackPressed();
        });
        if (editMode) {
            tvTitleNewTraining.setText(sectionUser.getData().getTitleDisplay());
        } else {
            tvTitleNewTraining.setText(getResources().getString(R.string.title_add_training));
        }

        adapter = new ExerciseAdapter(workoutUsers, this, this, this);
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onRemove(int pos) {
        workoutUsers.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(int pos) {
        posEdit = pos;
        Intent intent = new Intent(this, EditExerciseActivity.class);
        intent.putExtra("com/nhn/fitness/data", workoutUsers.get(pos));
        startActivityForResult(intent, 123);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                // Edit
                WorkoutUser workoutUser = data.getParcelableExtra("com/nhn/fitness/data");
                workoutUsers.set(posEdit, workoutUser);
                adapter.notifyDataSetChanged();
            } else if (requestCode == 122) {
                // Add new
                WorkoutUser workoutUser = data.getParcelableExtra("com/nhn/fitness/data");
                workoutUsers.add(workoutUser);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(workoutUsers, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        workoutUsers.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
