package com.nhn.fitness.ui.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.nhn.fitness.ui.base.BaseViewModel;

public class RunFragmentViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isRun;

    public RunFragmentViewModel() {
        super();
        isRun = new MutableLiveData<>();
    }
}
