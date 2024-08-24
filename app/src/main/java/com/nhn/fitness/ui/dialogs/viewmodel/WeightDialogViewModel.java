package com.nhn.fitness.ui.dialogs.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseViewModel;
import com.nhn.fitness.utils.Utils;

public class WeightDialogViewModel extends BaseViewModel {
    public MutableLiveData<Integer> unitType;
    public MutableLiveData<Float> weight;


    public WeightDialogViewModel() {
        super();
        unitType = new MutableLiveData<>(AppSettings.getInstance().getUnitType());
        float value = AppSettings.getInstance().getWeightDefault();
        if (AppSettings.getInstance().getUnitType() == 1) {
            value = Utils.convertKgToLbs(value);
        }
        weight = new MutableLiveData<>(value);
    }
}
