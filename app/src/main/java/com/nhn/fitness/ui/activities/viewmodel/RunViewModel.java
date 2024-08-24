package com.nhn.fitness.ui.activities.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.nhn.fitness.ui.base.BaseViewModel;
import com.nhn.fitness.ui.lib.timer.CountUpTimer;
import com.nhn.fitness.utils.SingleLiveEvent;

public class RunViewModel extends BaseViewModel {
    public SingleLiveEvent<Void> actionNext;
    public SingleLiveEvent<Void> actionPrev;
    public SingleLiveEvent<Void> actionQuit;
    public SingleLiveEvent<Void> actionBack;
    public SingleLiveEvent<Void> actionHelp;
    public SingleLiveEvent<Void> actionCloseHelp;
    public SingleLiveEvent<Void> actionSwitchAudioBackground;
    public MutableLiveData<Integer> actionPause;
    public MutableLiveData<Integer> actionResume;
    public MutableLiveData<Integer> current;

    public MutableLiveData<String> actionSpeech;

    public CountUpTimer countUpTimer;

    public RunViewModel() {
        super();
        actionNext = new SingleLiveEvent<>();
        actionPrev = new SingleLiveEvent<>();
        actionQuit = new SingleLiveEvent<>();
        actionBack = new SingleLiveEvent<>();
        actionHelp = new SingleLiveEvent<>();
        actionCloseHelp = new SingleLiveEvent<>();
        actionPause = new MutableLiveData<>();
        actionResume = new MutableLiveData<>();
        actionSwitchAudioBackground = new SingleLiveEvent<>();
        current = new MutableLiveData<>(0);
        actionSpeech = new MutableLiveData<>();

        countUpTimer = new CountUpTimer(false);
    }
}
