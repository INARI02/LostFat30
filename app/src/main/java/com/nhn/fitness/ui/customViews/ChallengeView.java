package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.data.repositories.ChallengeRepository;
import com.nhn.fitness.ui.activities.ChallengeActivity;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ChallengeView extends FrameLayout {
    private CompositeDisposable compositeDisposable;
    private ArrayList<ChallengeDayUser> list = new ArrayList<>();
    private int posPrepare = -1;

    public ChallengeView(Context context) {
        super(context);
        init();
    }

    public ChallengeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChallengeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChallengeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();
        View.inflate(getContext(), R.layout.group_challenge_layout, this);
        findViewById(R.id.card_view).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ChallengeActivity.class);
            getContext().startActivity(intent);
        });
        findViewById(R.id.btn_go).setVisibility(VISIBLE);
        findViewById(R.id.layout_progress).setVisibility(GONE);
        initData();
    }

    private void initData() {
        addDisposable(
                ChallengeRepository.getInstance().getAll().subscribe(response -> {
                    list.clear();
                    list.addAll(response);
                    updateProgress();
                })
        );
    }

    private void updateProgress() {
        posPrepare = -1;
        for (int i = 0; i < list.size(); i++) {
            ChallengeDayUser challengeDayUser = list.get(i);
            if (challengeDayUser.getState() == 1) {
                posPrepare = i;
                break;
            }
            if (i == 0) {
                if (challengeDayUser.getState() == 0) {
                    // Update prepare at 0
                    posPrepare = 0;
                    challengeDayUser.setState(1);
                    addDisposable(ChallengeRepository.getInstance().update(challengeDayUser).subscribe());
                    break;
                }
            } else {
                if (challengeDayUser.getState() == 0) {
                    if (list.get(i - 1).getState() == 2) {
                        // Update prepare at current
                        posPrepare = i;
                        challengeDayUser.setState(1);
                        addDisposable(ChallengeRepository.getInstance().update(challengeDayUser).subscribe());
                        break;
                    }
                }
            }
        }

        if (posPrepare != 0) {
            findViewById(R.id.btn_go).setVisibility(GONE);
            findViewById(R.id.layout_progress).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.btn_go).setVisibility(VISIBLE);
            findViewById(R.id.layout_progress).setVisibility(GONE);
        }

        // Update toolbar
        int daysLeft = 28 - posPrepare;
        int progress = posPrepare;
        if (posPrepare < 0) {
            daysLeft = 0;
            progress = 28;
        }
        ((TextView) findViewById(R.id.txt_days_left)).setText(String.format(getResources().getString(R.string.challange_days_left), daysLeft));
        ((TextView) findViewById(R.id.txt_progress)).setText(String.format(Locale.US, "%.0f", (progress / 28f) * 100f) + "%");
        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setMax(28);
        progressBar.setProgress(progress);
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.clear();
    }
}
