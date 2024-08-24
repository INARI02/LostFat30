package com.nhn.fitness.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable lastDisposable;
    protected BaseViewModel viewModel;
    protected View rootView;
    protected FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void initEvents() {

    }

    protected void initObservers() {

    }

    protected void initViews() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.clear();
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
        lastDisposable = disposable;
    }

    protected void popChildFragment(
            String TAG,
            int flag
    ) {
        if (null != TAG) {
            this.getChildFragmentManager().popBackStack(TAG, flag);
        } else {
            this.getChildFragmentManager().popBackStack();
        }
    }

    protected void popFragment(String TAG, int flag) {
        if (null != TAG) {
            requireActivity().getSupportFragmentManager().popBackStack(TAG, flag);
        } else {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    protected Fragment findFragment(String TAG) {
        return requireActivity().getSupportFragmentManager().findFragmentByTag(TAG);
    }

    protected Fragment findChildFragment(String TAG) {
        return this.getChildFragmentManager().findFragmentByTag(TAG);
    }

    public void addFragment(
            Fragment fragment, int containerViewId,
            String TAG, boolean addToBackStack,
            int transit
    ) {
        if (TAG != null && findFragment(TAG) != null) {
            popFragment(TAG, 0);
        } else {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(containerViewId, fragment, TAG);
            commitTransaction(transaction, TAG, addToBackStack, transit);
        }
    }

    public void replaceFragment(
            Fragment fragment, int containerViewId,
            String TAG, boolean addToBackStack,
            int transit
    ) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, TAG);
        commitTransaction(transaction, TAG, addToBackStack, transit);
    }

    public void replaceChildFragment(
            int containerViewId,
            Fragment fragment,
            String TAG,
            boolean addToBackStack,
            int transit
    ) {
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
        transaction.replace(
                containerViewId, fragment, TAG
        );
        commitTransaction(transaction, TAG, addToBackStack, transit);
    }

    public void addChildFragment(
            int containerViewId,
            Fragment fragment,
            String TAG,
            boolean addToBackStack,
            int transit
    ) {
        if (TAG != null && findChildFragment(TAG) != null) {
            popChildFragment(TAG, 0);
        } else {
            FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
            transaction.add(containerViewId, fragment, TAG);
            commitTransaction(transaction, TAG, addToBackStack, transit);
        }
    }

    private void commitTransaction(
            FragmentTransaction transaction, String TAG,
            boolean addToBackStack, int transit
    ) {
        if (addToBackStack) transaction.addToBackStack(TAG);
        if (transit != -1) transaction.setTransition(transit);
        transaction.commit();
    }

    protected void showDialogFragment(
            DialogFragment dialogFragment, String TAG,
            boolean addToBackStack, int transit
    ) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (addToBackStack) transaction.addToBackStack(TAG);
        if (transit != -1) transaction.setTransition(transit);
        dialogFragment.show(transaction, TAG);
    }

}
