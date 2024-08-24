package com.nhn.fitness.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.data.repositories.WorkoutRepository;
import com.nhn.fitness.ui.adapters.WorkoutAdapter;
import com.nhn.fitness.ui.adapters.helper.ItemTouchHelperAdapter;
import com.nhn.fitness.ui.adapters.helper.OnStartDragListener;
import com.nhn.fitness.ui.adapters.helper.SimpleItemTouchHelperCallback;
import com.nhn.fitness.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;


public class EditPlanActivity extends BaseActivity implements OnStartDragListener,
        ItemTouchHelperAdapter, DialogInterface.OnDismissListener, WorkoutAdapter.WorkoutChangeClickListener {
    private static final String TAG_NAME = EditPlanActivity.class.getSimpleName();
    private SectionUser sectionUser;
    private WorkoutAdapter adapter;
    private ArrayList<WorkoutUser> list = new ArrayList<>();
    private ArrayList<WorkoutUser> listEdited = new ArrayList<>();
    private boolean isLoaded = false;
    private int lastPosChange = -1;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_edit_plan);

        initViews();
        initEvents();
        initObservers();
    }

    private void initObservers() {
        loadListUser();
        loadListDefault();
    }

    private void loadListDefault() {
        Log.e("status", "load default");
        addDisposable(WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getData().getWorkoutsId())
                .subscribe(response -> {
                    list.clear();
                    list.addAll(response);
                }));
    }

    private void loadListUser() {
        Log.e("status", "load user");
        addDisposable(WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getWorkoutsId())
                .subscribe(response -> {
                    listEdited.clear();
                    listEdited.addAll(response);
                    adapter.notifyDataSetChanged();
                }));
    }

    private void initEvents() {
        findViewById(R.id.btn_save).setOnClickListener(view -> {
            ArrayList<String> workouts = new ArrayList<>();
            for (WorkoutUser workoutUser : listEdited) {
                workouts.add(workoutUser.getWorkoutUserId());
                if (workoutUser.getWorkoutUserId().contains("-")) {
                    addDisposable(WorkoutRepository.getInstance().insert(workoutUser).subscribe(() -> {
                        Log.e("status", "insert new: " + workoutUser.getWorkoutUserId());
                    }));
                }
            }
            sectionUser.setWorkoutsId(workouts);
            addDisposable(SectionRepository.getInstance()
                    .updateSectionUser(sectionUser)
                    .subscribe(this::onBackPressed, Throwable::printStackTrace));
        });

    }

    private void initViews() {
        sectionUser = getIntent().getParcelableExtra("com/nhn/fitness/data");

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new WorkoutAdapter(getSupportFragmentManager(), listEdited, this, this, this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.section_edit_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.menu_reset_section) {
            listEdited.clear();
            listEdited.addAll(list);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(listEdited, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        listEdited.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        loadListUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            WorkoutUser workoutUser = data.getParcelableExtra("result");
            if (workoutUser != null && lastPosChange != -1) {
                listEdited.set(lastPosChange, workoutUser);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onChange(int pos, WorkoutUser workoutUser) {
        lastPosChange = pos;
        Intent intent = new Intent(this, ReplaceActivity.class);
        intent.putExtra("section", sectionUser);
        intent.putExtra("workout", workoutUser);
        startActivityForResult(intent, 123);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
