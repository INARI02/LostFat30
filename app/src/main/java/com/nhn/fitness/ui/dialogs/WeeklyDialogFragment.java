package com.nhn.fitness.ui.dialogs;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyDialogFragment extends BaseDialog {
    private String[] list = new String[]{"1", "2", "3", "4", "5", "6", "7"};
    private int current = 3;
    private boolean saving = false;

    public WeeklyDialogFragment(int current) {
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.WeeklyDialog);
        this.current = current;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weekly_dialog, container, false);
        return rootView;
    }


    @Override
    protected void initViews() {
        if (current > 1) {
            ((TextView) rootView.findViewById(R.id.txt_day)).setText(getResources().getString(R.string.days));
        } else {
            ((TextView) rootView.findViewById(R.id.txt_day)).setText(getResources().getString(R.string.day));
        }

        NumberPicker np = rootView.findViewById(R.id.pk_number);
        np.setMinValue(0);
        np.setMaxValue(list.length - 1);
        np.setDisplayedValues(list);
        np.setWrapSelectorWheel(false);
        np.setValue(current - 1);
        np.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            if (newValue > 0) {
                ((TextView) rootView.findViewById(R.id.txt_day)).setText(getResources().getString(R.string.days));
            } else {
                ((TextView) rootView.findViewById(R.id.txt_day)).setText(getResources().getString(R.string.day));
            }
            current = newValue + 1;
        });

        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> {
            dismissAllowingStateLoss();
        });
        rootView.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            saving = true;
            dismissAllowingStateLoss();
        });
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogResultListener && saving) {
            ((DialogResultListener) activity).onResult(DialogResultListener.NUMBER_DAYS_WEEKLY, current);
        }
    }
}
