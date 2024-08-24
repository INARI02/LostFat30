package com.nhn.fitness.ui.dialogs;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstDayOfWeekDialogFragment extends BaseDialog {
    private String[] list = new String[3];
    private int current = Calendar.SUNDAY;
    private boolean saving = false;

    public FirstDayOfWeekDialogFragment(int current) {
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.WeeklyDialog);
        this.current = current;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_first_day_of_week_dialog, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        list[0] = getResources().getString(R.string.sunday);
        list[1] = getResources().getString(R.string.monday);
        list[2] = getResources().getString(R.string.saturday);

        NumberPicker np = rootView.findViewById(R.id.pk_number);
        np.setMinValue(0);
        np.setMaxValue(list.length - 1);
        np.setDisplayedValues(list);
        np.setWrapSelectorWheel(false);
        np.setValue(current == Calendar.SUNDAY ? 0 : current == Calendar.MONDAY ? 1 : 2);
        np.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            switch (newValue) {
                case 0:
                    current = Calendar.SUNDAY;
                    break;
                case 1:
                    current = Calendar.MONDAY;
                    break;
                case 2:
                    current = Calendar.SATURDAY;
                    break;
            }
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
            ((DialogResultListener) activity).onResult(DialogResultListener.FIRST_DAY_OF_WEEK, current);
        }
    }

}
