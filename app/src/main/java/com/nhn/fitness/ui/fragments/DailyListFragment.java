package com.nhn.fitness.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.MessageEvent;
import com.nhn.fitness.data.repositories.DailySectionRepository;
import com.nhn.fitness.ui.adapters.DailySectionAdapter;
import com.nhn.fitness.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyListFragment extends BaseFragment {

    private DailySectionAdapter adapter;
    private int level = 1;

    public DailyListFragment() {
        // doesn't do anything special
    }

    public DailyListFragment(int level) {
        this.level = level;
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
        rootView = inflater.inflate(R.layout.fragment_daily_list, container, false);
        initViews();
        initObservers();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        adapter = new DailySectionAdapter();
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initObservers() {
        super.initObservers();
        addDisposable(DailySectionRepository.getInstance().getListByLevel(level).subscribe(response -> {
//            for (DailySectionUser dailySectionUser : response) {
//                Log.e("status", dailySectionUser.getProgress() + "");
//            }
            adapter.setList(response);
        }));
    }
}
