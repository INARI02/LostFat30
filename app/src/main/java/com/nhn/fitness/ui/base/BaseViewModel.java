package com.nhn.fitness.ui.base;

import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends AndroidViewModel {

    public CompositeDisposable compositeDisposable;
    public Disposable lastDisposable;

    public BaseViewModel() {
        super(BaseApplication.getInstance());
        compositeDisposable = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
        lastDisposable = disposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
