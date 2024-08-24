package com.nhn.fitness.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.customViews.CheckDayView;

import java.util.ArrayList;
import java.util.List;

public class WeekOneFragment extends BaseFragment {
    private CheckDayView cvOne, cvTwo, cvThree, cvFour, cvFive, cvSix, cvSeven;
    private ImageView arOne, arTwo, arThree, arFour, arFive, arSix, imgCup;

    ArrayList<ChallengeDayUser> data = new ArrayList<>();

    public WeekOneFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_week_one, container, false);
        initViews();
        initEvents();
        initObservers();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        cvOne = rootView.findViewById(R.id.img_one);
        cvTwo = rootView.findViewById(R.id.img_two);
        cvThree = rootView.findViewById(R.id.img_three);
        cvFour = rootView.findViewById(R.id.img_four);
        cvFive = rootView.findViewById(R.id.img_five);
        cvSix = rootView.findViewById(R.id.img_six);
        cvSeven = rootView.findViewById(R.id.img_seven);
        imgCup = rootView.findViewById(R.id.img_eight);
        arOne = rootView.findViewById(R.id.arrow_one);
        arTwo = rootView.findViewById(R.id.arrow_two);
        arThree = rootView.findViewById(R.id.arrow_three);
        arFour = rootView.findViewById(R.id.arrow_four);
        arFive = rootView.findViewById(R.id.arrow_five);
        arSix = rootView.findViewById(R.id.arrow_six);

        refresh();
    }

    private void refresh() {
        if (data.size() == 7) {
            cvOne.setData(data.get(0));
            cvTwo.setData(data.get(1));
            cvThree.setData(data.get(2));
            cvFour.setData(data.get(3));
            cvFive.setData(data.get(4));
            cvSix.setData(data.get(5));
            cvSeven.setData(data.get(6));
            imgCup.setColorFilter(getResources().getColor(R.color.text_gray_light));
            updateArrow();
        } else {
            cvOne.reset();
            cvTwo.reset();
            cvThree.reset();
            cvFour.reset();
            cvFive.reset();
            cvSix.reset();
            cvSeven.reset();
            arOne.setColorFilter(getResources().getColor(R.color.text_gray_light));
            arTwo.setColorFilter(getResources().getColor(R.color.text_gray_light));
            arThree.setColorFilter(getResources().getColor(R.color.text_gray_light));
            arFour.setColorFilter(getResources().getColor(R.color.text_gray_light));
            arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
            arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
            imgCup.setColorFilter(getResources().getColor(R.color.text_gray_light));
        }
    }

    private void updateArrow() {
        int pos = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getState() == 1) {
                pos = i;
                break;
            }
            if (i == 6 && data.get(6).getState() == 2) {
                pos = 7;
            }
        }
        switch (pos) {
            case 0:
                arOne.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arTwo.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arThree.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFour.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 1:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arThree.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFour.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 2:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.blueLight));
                arThree.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFour.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 3:
            case 4:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.blueLight));
                arThree.setColorFilter(getResources().getColor(R.color.blueLight));
                arFour.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 5:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.blueLight));
                arThree.setColorFilter(getResources().getColor(R.color.blueLight));
                arFour.setColorFilter(getResources().getColor(R.color.blueLight));
                arFive.setColorFilter(getResources().getColor(R.color.text_gray_light));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 6:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.blueLight));
                arThree.setColorFilter(getResources().getColor(R.color.blueLight));
                arFour.setColorFilter(getResources().getColor(R.color.blueLight));
                arFive.setColorFilter(getResources().getColor(R.color.blueLight));
                arSix.setColorFilter(getResources().getColor(R.color.text_gray_light));
                break;
            case 7:
                arOne.setColorFilter(getResources().getColor(R.color.blueLight));
                arTwo.setColorFilter(getResources().getColor(R.color.blueLight));
                arThree.setColorFilter(getResources().getColor(R.color.blueLight));
                arFour.setColorFilter(getResources().getColor(R.color.blueLight));
                arFive.setColorFilter(getResources().getColor(R.color.blueLight));
                arSix.setColorFilter(getResources().getColor(R.color.blueLight));
                imgCup.setColorFilter(getResources().getColor(R.color.yellow));
                break;
        }
    }

    public void setData(List<ChallengeDayUser> list) {
//        Log.e("status", "set Week com.nhn.fitness.data");
        if (list.size() == 7) {
            data.clear();
            data.addAll(list);
            refresh();
        }
    }
}
