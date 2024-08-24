package com.nhn.fitness.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseViewModel;

public class RestSetViewModel extends BaseViewModel {
    public MutableLiveData<Integer> seconds;

    public RestSetViewModel(boolean isRestSet) {
        super();
        int value;
        if (isRestSet) {
            value = AppSettings.getInstance().getRestSet();
        } else {
            value = AppSettings.getInstance().getCountDown();
        }
        seconds = new MutableLiveData<>(value);
    }
}
