package com.nhn.fitness.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.MessageEvent;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.ui.activities.AddExerciseActivity;
import com.nhn.fitness.ui.adapters.TrainingAdapter;
import com.nhn.fitness.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ListTrainingFragment extends BaseFragment {
    private View btnAdd1, btnAdd2, layoutEmpty;
    private RecyclerView recyclerView;
    private ArrayList<SectionUser> sectionUsers = new ArrayList<>();
    private TrainingAdapter adapter;


    public ListTrainingFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getKey().equals(MessageEvent.CHANGE_GENDER)) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_training, container, false);
        initViews();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        btnAdd1 = rootView.findViewById(R.id.btn_add);
        btnAdd2 = rootView.findViewById(R.id.btn_add_2);
        layoutEmpty = rootView.findViewById(R.id.layout_empty);
        recyclerView = rootView.findViewById(R.id.rv_list);
        recyclerView.setVisibility(View.GONE);
        btnAdd2.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.VISIBLE);

        adapter = new TrainingAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        btnAdd1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddExerciseActivity.class);
            startActivity(intent);
        });
        btnAdd2.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddExerciseActivity.class);
            startActivity(intent);
        });
        addDisposable(
                SectionRepository.getInstance().getAllTrainingWithFullData()
                        .subscribe(response -> {
                            Log.e("status", "get training");
                            if (response == null || response.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                btnAdd2.setVisibility(View.GONE);
                                layoutEmpty.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                btnAdd2.setVisibility(View.VISIBLE);
                                layoutEmpty.setVisibility(View.GONE);

                                sectionUsers.clear();
                                sectionUsers.addAll(response);
                                adapter.setList(sectionUsers);
                            }
                        })
        );
    }
}
