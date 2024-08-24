package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.data.repositories.ChallengeRepository;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.fragments.WeekOneFragment;

import java.util.ArrayList;
import java.util.Locale;

public class ChallengeActivity extends BaseActivity {

    private WeekOneFragment weekOneFragment;
    private WeekOneFragment weekTwoFragment;
    private WeekOneFragment weekThreeFragment;
    private WeekOneFragment weekFourFragment;

    private View dvOne, dvTwo, dvThree;
    private ImageView imgCheckOne, imgCheckTwo, imgCheckThree, imgCheckFour;
    private TextView txtWeekOne, txtWeekTwo, txtWeekThree, txtWeekFour;
    private View stepOne, stepTwo, stepThree, stepFour;
    private TextView txtStepOne, txtStepTwo, txtStepThree, txtStepFour;

    private ArrayList<ChallengeDayUser> list = new ArrayList<>();
    private int posPrepare = -1;
    private int posWeek = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        initViews();
        initEvents();
        initObservers();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    private void initData() {
        addDisposable(
                ChallengeRepository.getInstance().getAll().subscribe(response -> {
                    list.clear();
                    list.addAll(response);
                    handleData();
                })
        );
    }

    private void handleData() {
        updateChallengePrepare();
        updateDivider();
//        Log.e("status", "pos: " + posPrepare + "");
//        Log.e("status", "state " + ChallengeRepository.getInstance().getChallengeDayUserWithFullDataWithoutObserve(list.get(posPrepare).getId()).getState());
    }

    private void updateDivider() {
        String posStep = (posPrepare + 1) % 7 + "";
        posWeek = (posPrepare + 1) / 7;
        if (posPrepare == -1) {
            posWeek = -1;
        }
        switch (posWeek) {
            case 0:
                dvOne.setBackgroundColor(getResources().getColor(R.color.blueLight));
                dvTwo.setBackgroundColor(getResources().getColor(R.color.text_gray_light));
                dvThree.setBackgroundColor(getResources().getColor(R.color.text_gray_light));
                filterColor(imgCheckOne, true);
                filterColor(imgCheckTwo, false);
                filterColor(imgCheckThree, false);
                filterColor(imgCheckFour, false);
                txtWeekOne.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekTwo.setTextColor(getResources().getColor(R.color.text_gray_light));
                txtWeekThree.setTextColor(getResources().getColor(R.color.text_gray_light));
                txtWeekFour.setTextColor(getResources().getColor(R.color.text_gray_light));
                stepOne.setVisibility(View.VISIBLE);
                stepTwo.setVisibility(View.GONE);
                stepThree.setVisibility(View.GONE);
                stepFour.setVisibility(View.GONE);
                txtStepOne.setText(posStep);
                break;
            case 1:
                dvOne.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvTwo.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvThree.setBackgroundColor(getResources().getColor(R.color.text_gray_light));
                filterColor(imgCheckOne, true);
                filterColor(imgCheckTwo, true);
                filterColor(imgCheckThree, false);
                filterColor(imgCheckFour, false);
                txtWeekOne.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekThree.setTextColor(getResources().getColor(R.color.text_gray_light));
                txtWeekFour.setTextColor(getResources().getColor(R.color.text_gray_light));
                stepOne.setVisibility(View.GONE);
                stepTwo.setVisibility(View.VISIBLE);
                stepThree.setVisibility(View.GONE);
                stepFour.setVisibility(View.GONE);
                txtStepTwo.setText(posStep);
                break;
            case 2:
                dvOne.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvTwo.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvThree.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                filterColor(imgCheckOne, true);
                filterColor(imgCheckTwo, true);
                filterColor(imgCheckThree, true);
                filterColor(imgCheckFour, false);
                txtWeekOne.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekThree.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekFour.setTextColor(getResources().getColor(R.color.text_gray_light));
                stepOne.setVisibility(View.GONE);
                stepTwo.setVisibility(View.GONE);
                stepThree.setVisibility(View.VISIBLE);
                stepFour.setVisibility(View.GONE);
                txtStepThree.setText(posStep);
                break;
            case 3:
                dvOne.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvTwo.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvThree.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                filterColor(imgCheckOne, true);
                filterColor(imgCheckTwo, true);
                filterColor(imgCheckThree, true);
                filterColor(imgCheckFour, true);
                txtWeekOne.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekThree.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekFour.setTextColor(getResources().getColor(R.color.colorAccent));
                stepOne.setVisibility(View.GONE);
                stepTwo.setVisibility(View.GONE);
                stepThree.setVisibility(View.GONE);
                stepFour.setVisibility(View.VISIBLE);
                txtStepFour.setText(posStep);
                break;
            default:
                dvOne.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvTwo.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                dvThree.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                filterColor(imgCheckOne, true);
                filterColor(imgCheckTwo, true);
                filterColor(imgCheckThree, true);
                filterColor(imgCheckFour, true);
                txtWeekOne.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekThree.setTextColor(getResources().getColor(R.color.colorAccent));
                txtWeekFour.setTextColor(getResources().getColor(R.color.colorAccent));
                stepOne.setVisibility(View.GONE);
                stepTwo.setVisibility(View.GONE);
                stepThree.setVisibility(View.GONE);
                stepFour.setVisibility(View.GONE);
                break;
        }

        // Update toolbar
        int daysLeft = 28 - posPrepare;
        int progress = posPrepare;
        if (posPrepare < 0) {
            daysLeft = 0;
            progress = 28;
        }
        ((TextView) findViewById(R.id.txt_days_left)).setText(String.format(Locale.US, getResources().getString(R.string.challange_days_left), daysLeft));
        ((TextView) findViewById(R.id.txt_progress)).setText(getResources().getString(R.string.progress) + String.format(" %.0f", (progress / 28f) * 100f) + "%");
        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setMax(28);
        progressBar.setProgress(progress);
    }

    private void updateChallengePrepare() {
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
//        Log.e("status", posPrepare + "");
        loadData();
    }


    private void loadData() {
        addDisposable(
                ChallengeRepository.getInstance().getChallengeDayUserWithFullDataByWeek(1)
                        .subscribe(response -> {
                            weekOneFragment.setData(response);
                        })
        );
        addDisposable(
                ChallengeRepository.getInstance().getChallengeDayUserWithFullDataByWeek(2)
                        .subscribe(response -> {
                            weekTwoFragment.setData(response);
                        })
        );
        addDisposable(
                ChallengeRepository.getInstance().getChallengeDayUserWithFullDataByWeek(3)
                        .subscribe(response -> {
                            weekThreeFragment.setData(response);
                        })
        );
        addDisposable(
                ChallengeRepository.getInstance().getChallengeDayUserWithFullDataByWeek(4)
                        .subscribe(response -> {
                            weekFourFragment.setData(response);
                        })
        );
    }

    private void initObservers() {

    }

    private void initEvents() {
        findViewById(R.id.btn_start).setOnClickListener(view -> {
            ChallengeDayUser challengeDayUser;
            if (posPrepare == -1) {
                challengeDayUser = ChallengeRepository.getInstance().getChallengeDayUserWithFullDataWithoutObserve(list.get(0).getId());
            } else {
                challengeDayUser = ChallengeRepository.getInstance().getChallengeDayUserWithFullDataWithoutObserve(list.get(posPrepare).getId());
            }
            addDisposable(
                    SectionRepository.getInstance().getSectionUserByIdWithFullData(challengeDayUser.getData().getSectionId())
                            .subscribe(response -> {
                                Intent intent = new Intent(this, SectionDetailActivity.class);
                                intent.putExtra("com/nhn/fitness/data", response);
                                intent.putExtra("challenge", challengeDayUser);
                                startActivity(intent);
                            })
            );
        });
    }

    private void initViews() {
        initToolbar();

        weekOneFragment = new WeekOneFragment();
        weekTwoFragment = new WeekOneFragment();
        weekThreeFragment = new WeekOneFragment();
        weekFourFragment = new WeekOneFragment();

        addFragment(weekOneFragment, R.id.week_one, null, false, -1);
        addFragment(weekTwoFragment, R.id.week_two, null, false, -1);
        addFragment(weekThreeFragment, R.id.week_three, null, false, -1);
        addFragment(weekFourFragment, R.id.week_four, null, false, -1);

        dvOne = findViewById(R.id.dv_one);
        dvTwo = findViewById(R.id.dv_two);
        dvThree = findViewById(R.id.dv_three);

        imgCheckOne = findViewById(R.id.img_check_week_one);
        imgCheckTwo = findViewById(R.id.img_check_week_two);
        imgCheckThree = findViewById(R.id.img_check_week_three);
        imgCheckFour = findViewById(R.id.img_check_week_four);

        txtWeekOne = findViewById(R.id.txt_week_one);
        txtWeekTwo = findViewById(R.id.txt_week_two);
        txtWeekThree = findViewById(R.id.txt_week_three);
        txtWeekFour = findViewById(R.id.txt_week_four);

        stepOne = findViewById(R.id.step_one);
        stepTwo = findViewById(R.id.step_two);
        stepThree = findViewById(R.id.step_three);
        stepFour = findViewById(R.id.step_four);

        txtStepOne = findViewById(R.id.txt_step_one);
        txtStepTwo = findViewById(R.id.txt_step_two);
        txtStepThree = findViewById(R.id.txt_step_three);
        txtStepFour = findViewById(R.id.txt_step_four);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void filterColor(ImageView imageView, boolean isEnable) {
        if (isEnable) {
            imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
        } else {
            imageView.setColorFilter(getResources().getColor(R.color.text_gray_light));
        }
    }
}
