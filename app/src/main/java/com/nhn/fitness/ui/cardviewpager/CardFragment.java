package com.nhn.fitness.ui.cardviewpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.repositories.DailySectionRepository;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.fragments.DailyListFragment;
import com.nhn.fitness.ui.fragments.ListTrainingFragment;
import com.nhn.fitness.utils.ViewUtils;

import java.util.List;


public class CardFragment extends BaseFragment {

    private int position;

    public static CardFragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 1);
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_viewpager, container, false);
        initViews();
        initObservers();
        return rootView;
    }

    @Override
    protected void initObservers() {
        super.initObservers();
        if (position < 4) {
            addDisposable(
                    DailySectionRepository.getInstance()
                            .getListByLevel(position)
                            .subscribe(response -> {
                                updateViews(response);
                            })
            );
        } else {
            // For training
        }
    }

    private void updateViews(List<DailySectionUser> response) {
        ProgressBar progressBar = rootView.findViewById(R.id.progress_horizontal);
        TextView txtProgress = rootView.findViewById(R.id.txt_progress);
        int current = 0;
        for (DailySectionUser dailySectionUser : response) {
            if (dailySectionUser.isCompleted()) {
                current++;
            }
        }
        progressBar.setProgress(current);
        txtProgress.setText(current + "/30");
    }

    @Override
    protected void initViews() {
        super.initViews();

        switch (position) {
            case 1: {
                replaceChildFragment(R.id.container, new DailyListFragment(1), null, false, -1);
                break;
            }
            case 2: {
                replaceChildFragment(R.id.container, new DailyListFragment(2), null, false, -1);
                break;
            }
            case 3: {
                replaceChildFragment(R.id.container, new DailyListFragment(3), null, false, -1);
                break;
            }
            case 4:
            default: {
                replaceChildFragment(R.id.container, new ListTrainingFragment(), null, false, -1);
                break;
            }
        }

        // update icon
        View layout1, layout2, layout3;
        ImageView ivLevel1, ivLevel2, ivLevel3, imgThumb;
        TextView txtTitle, txtDescription;
        ivLevel1 = rootView.findViewById(R.id.ivLevel1);
        ivLevel2 = rootView.findViewById(R.id.ivLevel2);
        ivLevel3 = rootView.findViewById(R.id.ivLevel3);
        imgThumb = rootView.findViewById(R.id.img_thumb);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtDescription = rootView.findViewById(R.id.txt_description);
        layout1 = rootView.findViewById(R.id.layout_1);
        layout2 = rootView.findViewById(R.id.layout_2);
        layout3 = rootView.findViewById(R.id.layout_3);
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.VISIBLE);
        layout3.setVisibility(View.GONE);
        if (position == 1) {
            txtTitle.setText(getResources().getString(R.string.title_full_body_level_1));
            txtDescription.setText(getResources().getString(R.string.description_full_body_level_1));
            ivLevel1.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ViewUtils.bindImage(getContext(), ViewUtils.getPathSection("belly1"), imgThumb);
        } else if (position == 2) {
            txtTitle.setText(getResources().getString(R.string.title_full_body_level_2));
            txtDescription.setText(getResources().getString(R.string.description_full_body_level_2));
            ivLevel1.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ivLevel2.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ViewUtils.bindImage(getContext(), ViewUtils.getPathSection("belly2"), imgThumb);
        } else if (position == 3) {
            txtTitle.setText(getResources().getString(R.string.title_full_body_level_3));
            txtDescription.setText(getResources().getString(R.string.description_full_body_level_3));
            ivLevel1.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ivLevel2.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ivLevel3.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ViewUtils.bindImage(getContext(), ViewUtils.getPathSection("belly3"), imgThumb);
        } else {
            // For training
            ViewUtils.bindImage(getContext(), ViewUtils.getPathSection("mytranning"), imgThumb);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);
        }
    }

    public View getCardView() {
        return rootView;
    }
}
