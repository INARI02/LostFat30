package com.nhn.fitness.ui.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.activities.RunActivity;
import com.nhn.fitness.ui.activities.viewmodel.RunViewModel;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.base.MyViewModelFactory;
import com.nhn.fitness.ui.customViews.StepProgressBarView;
import com.nhn.fitness.ui.dialogs.SoundOptionDialog;
import com.nhn.fitness.ui.fragments.viewmodel.RunFragmentViewModel;
import com.nhn.fitness.ui.lib.timer.CountUpTimer;
import com.nhn.fitness.ui.lib.timer.Hourglass;
import com.nhn.fitness.utils.Utils;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RunFragment extends BaseFragment {
    private RunFragmentViewModel viewModel;
    private RunViewModel activityViewModel;
    private int restTime;

    private View restLayout, runLayout;
    private TextView txtTotalTimer, txtTimerDown, txtTimerDownRest;
    private ProgressBar progressRun, progressRest;

    private Hourglass runTimer, restTimer, restProgressTimer;
    private float oldY = 0f;
    private StepProgressBarView stepProgress;
    private WorkoutUser data;
    private ArrayList<WorkoutUser> listData;
    private int total, pos;
    private boolean modeCounter = false;
    private boolean isVisible = true;

    private CountUpTimer countUpTimer;

//    private ObjectAnimator objectAnimatorAudioBackground;

    private RunFragment() {
    }

    public static RunFragment newInstance(ArrayList<WorkoutUser> workoutUsers, WorkoutUser workoutUser, int total, int pos) {
        RunFragment fragment = new RunFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("listData", workoutUsers);
        args.putParcelable("com/nhn/fitness/data", workoutUser);
        args.putInt("total", total);
        args.putInt("pos", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public void reset() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, MyViewModelFactory.getInstance()).get(RunFragmentViewModel.class);
        activityViewModel = ViewModelProviders.of(getActivity(), MyViewModelFactory.getInstance()).get(RunViewModel.class);
        if (getArguments() != null) {
            listData = getArguments().getParcelableArrayList("listData");
            data = getArguments().getParcelable("com/nhn/fitness/data");
            total = getArguments().getInt("total");
            pos = getArguments().getInt("pos");
            modeCounter = data.getData().getType() == 1;
        }
        restTime = AppSettings.getInstance().getRestSet();
        if (pos == 0) {
            restTime = AppSettings.getInstance().getCountDown();
        }

            countUpTimer = new CountUpTimer(modeCounter);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_run, container, false);
        initViews();
        initEvents();
        initObservers();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("status", "resume " + pos);
       // runAnimationAudioBackground(AppSettings.getInstance().isPlayAudioBackground());
    }

    @Override
    protected void initViews() {
        super.initViews();
        stepProgress = rootView.findViewById(R.id.step_progress);
        stepProgress.setup(listData.size());
        stepProgress.progress(pos);
      //  replaceChildFragment(R.id.ad_container, new AdsFragment(), null, false, -1);

        // Init mode

        if (pos == 0) {
            //rootView.findViewById(R.id.btn_prev_run).setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.btn_prev_take_a_rest).setVisibility(View.INVISIBLE);
          //  rootView.findViewById(R.id.txt_take_a_rest).setVisibility(View.GONE);
           // rootView.findViewById(R.id.txt_ready_to_go).setVisibility(View.VISIBLE);
        } else {
           // rootView.findViewById(R.id.txt_take_a_rest).setVisibility(View.VISIBLE);
           // rootView.findViewById(R.id.txt_ready_to_go).setVisibility(View.GONE);
        }
        if (modeCounter) {
            rootView.findViewById(R.id.info_timer).setVisibility(View.GONE);
            rootView.findViewById(R.id.txt_workout_count).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.btn_next_run).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.btn_play).setVisibility(View.GONE);
            rootView.findViewById(R.id.btn_pause).setVisibility(View.GONE);
            rootView.findViewById(R.id.btn_checked).setVisibility(View.VISIBLE);
            //rootView.findViewById(R.id.progress_timer_run).setVisibility(View.INVISIBLE);
        } else {
            rootView.findViewById(R.id.info_timer).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.txt_workout_count).setVisibility(View.GONE);
            rootView.findViewById(R.id.btn_next_run).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.btn_play).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.btn_checked).setVisibility(View.GONE);
           // rootView.findViewById(R.id.progress_timer_run).setVisibility(View.VISIBLE);
        }
        restLayout = rootView.findViewById(R.id.take_a_rest_layout);
        runLayout = rootView.findViewById(R.id.run_layout);
        txtTimerDown = rootView.findViewById(R.id.txt_timer_down);
       // txtTotalTimer = rootView.findViewById(R.id.txt_total_timer);
        txtTimerDownRest = rootView.findViewById(R.id.txt_timer_take_a_rest);
        bindData();

        View numThree = rootView.findViewById(R.id.txt_num_three);
        numThree.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                numThree.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                oldY = numThree.getTranslationY();
            }
        });

        // Animation of audio background icon
        // View audioBackground = rootView.findViewById(R.id.btn_audio_background);
//        objectAnimatorAudioBackground = ObjectAnimator.ofFloat(audioBackground, "rotation", 0f, 360f);
//        objectAnimatorAudioBackground.setDuration(1000);
//        objectAnimatorAudioBackground.setRepeatCount(Animation.INFINITE);
//        objectAnimatorAudioBackground.setInterpolator(new LinearInterpolator());
    }

    private void bindData() {
        // Progress Rest
        progressRest = rootView.findViewById(R.id.progress_timer_take_a_rest);
        progressRest.setMax(restTime * 1000);
        ((TextView) rootView.findViewById(R.id.txt_workout_name)).setText(Utils.changeUpperString(data.getData().getTitleDisplay()));
        String titleNext;
        if (modeCounter) {
            titleNext = String.format(Locale.US, "%d/%d %s", pos + 1, total, Utils.changeUpperString(data.getData().getTitleDisplay()));
            ((TextView) rootView.findViewById(R.id.txt_workout_count)).setText("x" + data.getCount());
        } else {
            titleNext = String.format(Locale.US, "%d/%d %s", pos + 1, total, Utils.changeUpperString(data.getData().getTitleDisplay()));
           // txtTotalTimer.setText("/" + com.nhn.fitness.data.getTime() + "\"");
            txtTimerDown.setText(data.getTime() + "");

            // Progress Run
//            progressRun = rootView.findViewById(R.id.progress_timer_run);
//            progressRun.setMax(com.nhn.fitness.data.getTime());
//            progressRun.setProgress(0);
        }
        ((TextView) rootView.findViewById(R.id.txt_title_next_workout)).setText(titleNext);
       // ImageView thumbNext = rootView.findViewById(R.id.img_thumb_next);
        //ViewUtils.bindImage(getContext(), ViewUtils.getPathWorkout(com.nhn.fitness.data.getData().getImageGender()), thumbNext);
        ImageView thumbRun = rootView.findViewById(R.id.img_thumb_run);
        ViewUtils.bindImage(getContext(), ViewUtils.getPathWorkout(data.getData().getImageGender()), thumbRun);

    }

    @Override
    protected void initObservers() {
        super.initObservers();
        Log.e("status", "initObservers " + pos);
        viewModel.isRun.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    switchToRun();
                } else {
                    switchToRest();
                }
            }
        });
        activityViewModel.current.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                releaseAllTimer();
                if (pos == integer) {
                    viewModel.isRun.setValue(false);
                }
            }
        });
        activityViewModel.actionPause.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer aVoid) {
                isVisible = false;
                Log.e("status", "change pause: " + pos);
                if (restTimer != null) {
                    if (restTimer.isRunning()) {
                        Log.e("status", "isPause " + pos);
                        restTimer.pauseTimer();
                    } else {
                        Log.e("status", "null " + pos);
                    }
                }
                if (restProgressTimer != null) {
                    if (restProgressTimer.isRunning()) {
                        restProgressTimer.pauseTimer();
                    }
                }
                if (runTimer != null) {
                    if (runTimer.isRunning()) {
                        runTimer.pauseTimer();
                    }
                }
            }
        });
        activityViewModel.actionResume.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer aVoid) {
                isVisible = true;
                Log.e("status", "change resume " + pos);
                if (restTimer != null) {
                    if (restTimer.isPaused()) {
                        Log.e("status", "isResume" + pos);
                        restTimer.resumeTimer();
                    }
                }
                if (restProgressTimer != null) {
                    if (restProgressTimer.isPaused()) {
                        restProgressTimer.resumeTimer();
                    }
                }
                if (runTimer != null) {
                    if (runTimer.isPaused()) {
                        runTimer.resumeTimer();
                    } else if (!runTimer.isRunning()) {
                        Log.e("status", "resume and run animation " + pos);
                        if (!modeCounter) {
                            runAnimation();
                        }
                    }
                } else {
                    Log.e("status", "resume and run animation " + pos);
                    if (!modeCounter) {
                        runAnimation();
                    }
                }
            }
        });
    }

    private void switchToRest() {
        runLayout.setVisibility(View.GONE);
        restLayout.setVisibility(View.VISIBLE);
        if (restProgressTimer != null) {
            restProgressTimer.stopTimer();
        }
        restProgressTimer = new Hourglass(restTime * 1000, 80) {
            @Override
            public void onTimerTick(long timeRemaining) {
                progressRest.setProgress((int) (restTime * 1000 - timeRemaining));
            }

            @Override
            public void onTimerFinish() {

            }
        };
        restProgressTimer.startTimer();
        if (restTimer == null) {
            restTimer = new Hourglass(restTime * 1000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {
                    txtTimerDownRest.setText(timeRemaining / 1000 + "");
                    if (timeRemaining <= 3100 && timeRemaining > 100) {
                        speech(timeRemaining / 1000 + "");
                    }
                }

                @Override
                public void onTimerFinish() {
                    Log.e("status", "rest " + pos + " finished");
                    skipRest();
                }
            };
        } else {
            restTimer.release();
        }
        restTimer.startTimer();
        Log.e("status", "rest " + pos + " start");

        // Speech
        if (pos == 0) {
            speech(getResources().getString(R.string.ready_to_to_start_with) + " " + data.getData().getTitleDisplay());
        } else {
            String time;
            if (modeCounter) {
                time = data.getTotalCount() + ". ";
            } else {
                time = data.getTime() + ". " + getResources().getString(R.string.seconds) + ". ";
            }
            speech(getResources().getString(R.string.take_a_rest_next) + " " + time + data.getData().getTitleDisplay());
        }
    }

    @SuppressLint("CheckResult")
    private void speech(String text) {
        if (activityViewModel.current.getValue() != pos) return;
        if (pos == 0) {
            Flowable.just(0).delay(1500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        activityViewModel.actionSpeech.postValue(text);
                    });
        } else {
            activityViewModel.actionSpeech.postValue(text);
        }
    }

    private void skipRest() {
        if (activityViewModel.current.getValue() == pos) {
            viewModel.isRun.setValue(true);
        }
    }

    private void switchToRun() {
        runLayout.setVisibility(View.VISIBLE);
        restLayout.setVisibility(View.GONE);
        if (!modeCounter) {
            runAnimation();
        } else {
            countUpTimer.resume();
            activityViewModel.countUpTimer.resume();
            startSpeechRun();
        }
    }

    private void startRun() {
        Log.e("status", "start run " + pos);
        if (!modeCounter) {
            txtTimerDown.setText(data.getTime() + "");
          //  progressRun.setProgress(0);
            if (runTimer == null) {
                runTimer = new Hourglass(data.getTime() * 1000, 1000) {
                    @Override
                    public void onTimerTick(long timeRemaining) {
                        txtTimerDown.setText(timeRemaining / 1000 + "");
                      //  progressRun.setProgress((int) (com.nhn.fitness.data.getTime() - (timeRemaining / 1000)));
                        if (timeRemaining == 10000) {
                            speech(getResources().getString(R.string.ten_seconds_left));
                        }
                        if (timeRemaining <= 3100 && timeRemaining > 100) {
                            speech(timeRemaining / 1000 + "");
                        }
                    }

                    @Override
                    public void onTimerFinish() {
                        Log.e("status", "timer " + pos + " finished");
                        onClickNext();
                    }
                };
            } else {
                runTimer.release();
            }
            activityViewModel.countUpTimer.resume();
            runTimer.startTimer();
        }

        startSpeechRun();
    }

    private void startSpeechRun() {
        // Speech
        String time;
        if (modeCounter) {
            time = data.getTotalCount() + ". ";
        } else {
            time = data.getTime() + ". " + getResources().getString(R.string.seconds) + ". ";
        }
        speech(getResources().getString(R.string.start) + ". " + time + data.getData().getTitleDisplay());
    }

    private void playRings() {
        if (AppSettings.getInstance().getSound() && AppSettings.getInstance().getSoundVoice()) {
            MediaPlayer.create(getContext(), R.raw.rings).start();
        }
    }

    private void runAnimation() {
        txtTimerDown.setText(data.getTime() + "");
      //  progressRun.setProgress(0);
        Log.e("status", "runAnimation " + pos);
        View numThree = rootView.findViewById(R.id.txt_num_three);
        View numTwo = rootView.findViewById(R.id.txt_num_two);
        View numOne = rootView.findViewById(R.id.txt_num_one);
        ObjectAnimator alphaOne = ObjectAnimator.ofFloat(numOne, "alpha", 1, 0f);
        alphaOne.setDuration(1000);
        alphaOne.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator moveOneY = ObjectAnimator.ofFloat(numOne, "translationY", 500f);
        moveOneY.setDuration(1000);
        moveOneY.setInterpolator(new AccelerateDecelerateInterpolator());
        moveOneY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                numOne.setVisibility(View.VISIBLE);
                numOne.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                numOne.setVisibility(View.INVISIBLE);
                numOne.setTranslationY(oldY);
                if (isVisible) {
                    startRun();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator alphaTwo = ObjectAnimator.ofFloat(numTwo, "alpha", 1, 0f);
        alphaTwo.setDuration(1000);
        alphaTwo.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator moveTwoY = ObjectAnimator.ofFloat(numTwo, "translationY", 500f);
        moveTwoY.setDuration(1000);
        moveTwoY.setInterpolator(new AccelerateDecelerateInterpolator());
        moveTwoY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                numTwo.setVisibility(View.VISIBLE);
                numTwo.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isVisible) {
                    moveOneY.start();
                    alphaOne.start();
                }
                numTwo.setVisibility(View.INVISIBLE);
                numTwo.setTranslationY(oldY);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator alphaThree = ObjectAnimator.ofFloat(numThree, "alpha", 1, 0f);
        alphaThree.setDuration(1000);
        alphaThree.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator moveThreeY = ObjectAnimator.ofFloat(numThree, "translationY", 500f);
        moveThreeY.setDuration(1000);
        moveThreeY.setInterpolator(new AccelerateDecelerateInterpolator());
        moveThreeY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                numThree.setVisibility(View.VISIBLE);
                numThree.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isVisible) {
                    moveTwoY.start();
                    alphaTwo.start();
                }
                numThree.setVisibility(View.INVISIBLE);
                numThree.setTranslationY(oldY);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        if (isVisible) {
            moveThreeY.start();
            alphaThree.start();
        }
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_prev_run).setOnClickListener(view -> {
            onClickPrev();
        });
        rootView.findViewById(R.id.btn_prev_take_a_rest).setOnClickListener(view -> {
            onClickPrev();
        });
        rootView.findViewById(R.id.btn_next_run).setOnClickListener(view -> {
            if (rootView.findViewById(R.id.txt_num_one).getVisibility() == View.INVISIBLE &&
                    rootView.findViewById(R.id.txt_num_two).getVisibility() == View.INVISIBLE &&
                    rootView.findViewById(R.id.txt_num_three).getVisibility() == View.INVISIBLE) {
                onClickNext();
            }
        });
        rootView.findViewById(R.id.btn_checked).setOnClickListener(view -> {
            onClickNext();
        });
        rootView.findViewById(R.id.btn_next_take_a_rest).setOnClickListener(view -> {
            if (restTimer != null && restTimer.isRunning()) {
                restTimer.stopTimer();
            }
        });
        rootView.findViewById(R.id.btn_video).setOnClickListener(view -> {
            activityViewModel.actionHelp.call();
        });
        rootView.findViewById(R.id.layout_video).setOnClickListener(view -> {
            activityViewModel.actionHelp.call();
        });
        rootView.findViewById(R.id.btn_play).setOnClickListener(view -> {
            if (runTimer == null) return;
            rootView.findViewById(R.id.btn_play).setVisibility(View.GONE);
            rootView.findViewById(R.id.btn_pause).setVisibility(View.VISIBLE);
            runTimer.resumeTimer();
        });
        rootView.findViewById(R.id.btn_pause).setOnClickListener(view -> {
            if (runTimer == null) return;
            rootView.findViewById(R.id.btn_pause).setVisibility(View.GONE);
            rootView.findViewById(R.id.btn_play).setVisibility(View.VISIBLE);
            runTimer.pauseTimer();
        });
        rootView.findViewById(R.id.btn_back).setOnClickListener(view -> {
            activityViewModel.actionBack.call();
        });
        rootView.findViewById(R.id.btn_audio_background).setOnClickListener(view -> {
            switchAudioBackground();
        });
        rootView.findViewById(R.id.btn_audio_settings).setOnClickListener(view -> {
            new SoundOptionDialog().show(getChildFragmentManager(), null);
        });
    }

    private void switchAudioBackground() {
        boolean current = AppSettings.getInstance().isPlayAudioBackground();
        current = !current;
        AppSettings.getInstance().setPlayAudioBackground(current);
        runAnimationAudioBackground(current);
        activityViewModel.actionSwitchAudioBackground.call();
    }

    private void runAnimationAudioBackground(boolean isEnable) {
        View audioBackground = rootView.findViewById(R.id.btn_audio_background);
        if (isEnable) {
            audioBackground.setBackgroundResource(R.drawable.ic_music_on);
        } else {
            audioBackground.setBackgroundResource(R.drawable.ic_music_off);
        }
    }

    private void onClickNext() {
        if (modeCounter) {
            float weight = AppSettings.getInstance().getWeightDefault();
            int time = countUpTimer.getTime() / 1000;
            ((RunActivity) getActivity()).setCalories(pos, data.getData().getCalories() * weight * time);
        }
        playRings();
        releaseAllTimer();
        if (activityViewModel.current.getValue() == pos) {
            activityViewModel.countUpTimer.pause();
            activityViewModel.actionNext.call();
        }
    }

    private void onClickPrev() {
        releaseAllTimer();
        if (activityViewModel.current.getValue() == pos) {
            activityViewModel.countUpTimer.pause();
            activityViewModel.actionPrev.call();
        }
    }

    private void releaseAllTimer() {
        if (modeCounter) {
            countUpTimer.pause();
            countUpTimer.reset();
        }
        if (restTimer != null) {
            restTimer.release();
        }
        if (restProgressTimer != null) {
            restProgressTimer.release();
        }
        if (runTimer != null) {
            runTimer.release();
        }
    }
}
