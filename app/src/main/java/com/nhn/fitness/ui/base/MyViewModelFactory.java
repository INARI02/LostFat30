package com.nhn.fitness.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nhn.fitness.ui.activities.viewmodel.RunViewModel;
import com.nhn.fitness.ui.fragments.viewmodel.RunFragmentViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    public static MyViewModelFactory instance;

    public static MyViewModelFactory getInstance() {
        if (instance == null) {
            instance = new MyViewModelFactory();
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RunViewModel.class)) {
            return (T) new RunViewModel();
        } else if (modelClass.isAssignableFrom(RunFragmentViewModel.class)) {
            return (T) new RunFragmentViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
