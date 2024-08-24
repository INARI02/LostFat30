package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.MessageEvent;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.ui.activities.SectionDetailActivity;
import com.nhn.fitness.ui.adapters.SectionAdapter;
import com.nhn.fitness.ui.adapters.diff.SectionUserDiffCallBack;
import com.nhn.fitness.ui.interfaces.SectionItemClickListener;
import com.nhn.fitness.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GroupSections extends FrameLayout implements SectionItemClickListener {
    private SectionAdapter adapter;

    public GroupSections(Context context) {
        super(context);
    }

    public GroupSections(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GroupSections(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GroupSections(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        View.inflate(getContext(), R.layout.group_workouts_layout, this);

        findViewById(R.id.btn_go).setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.OPEN_WORKOUT_EVENT, null));
        });

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setItemViewCacheSize(13);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SectionAdapter(new SectionUserDiffCallBack(), this,getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setTitle(String title) {
        ((TextView) findViewById(R.id.txt_title)).setText(title);
    }

    public void setSections(List<SectionUser> list) {
//        Log.e("status", "refresh");
        if (list.isEmpty()) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
            findViewById(R.id.rv_list).setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
            findViewById(R.id.rv_list).setVisibility(View.VISIBLE);
            adapter.submitList(list);
        }
    }

    @Override
    public void onSectionClick(View view, SectionUser sectionUser, int position) {
        Intent intent = new Intent(getContext(), SectionDetailActivity.class);
        intent.putExtra("com/nhn/fitness/data", sectionUser);

        ImageView imageView = view.findViewById(R.id.img_thumb);
        View maskView = view.findViewById(R.id.mask);
        View titleView = view.findViewById(R.id.txt_title);
        View workoutView = view.findViewById(R.id.layout_workout);
        Pair<View, String> thumb = Pair.create(imageView, "thumb");
        Pair<View, String> mask = Pair.create(maskView, "mask");
        Pair<View, String> title = Pair.create(titleView, "title");
//        Pair<View, String> workout = Pair.create(workoutView, "workout");

        ActivityOptionsCompat compat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(ViewUtils.getActivity(getContext()), thumb, mask, title);
        ActivityCompat.startActivity(getContext(), intent, compat.toBundle());
    }
}
