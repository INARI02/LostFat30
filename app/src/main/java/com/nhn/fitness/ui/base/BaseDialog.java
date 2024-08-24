package com.nhn.fitness.ui.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseDialog extends DialogFragment {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected View rootView;
    protected boolean hasTitle = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new AppCompatDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
        if (!hasTitle) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initViews();
        initEvents();
        initObserve();
    }

    protected void initData(){

    }

    protected void initViews() {

    }

    protected void initEvents() {

    }

    protected void initObserve() {

    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        compositeDisposable.clear();
    }
}
