package com.nhn.fitness.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.dialogs.BMIDialogFragment;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.Utils;

import java.util.Locale;


public class BMIFragment extends BaseFragment implements DialogResultListener {
    private float widthMax = 0;
    private View btnThumb;
    private TextView txtThumb, txtStatus;

    public BMIFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bmi, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
//        addDisposable(DayHistoryRepository.getInstance().getWeightNewest().subscribe(response -> {
//            Log.e("status", "newest waistline: " + response.getWeight());
//        }, Throwable::printStackTrace));

        txtStatus = rootView.findViewById(R.id.txt_status);
        btnThumb = rootView.findViewById(R.id.btn_thumb);
        txtThumb = rootView.findViewById(R.id.txt_thumb);
        View image = rootView.findViewById(R.id.img_thumb);
        image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                widthMax = image.getWidth();
                calBMI();
            }
        });
        updateHeight();
    }

    private void updateHeight() {
        float height = AppSettings.getInstance().getHeightDefault();
        String unit = "CM";
        int type = AppSettings.getInstance().getUnitType();
        if (type == 1) {
            height = Utils.convertCmToFt(height);
            unit = "FT";
        }
        ((TextView) rootView.findViewById(R.id.txt_height_value)).setText(String.format(Locale.US, "%.01f %s", height, unit));
    }

    private void calBMI() {
        float height = AppSettings.getInstance().getHeightDefault();
        float weight = AppSettings.getInstance().getWeightDefault();
        float bmi = Utils.calculatorBMI(height, weight);
        if (bmi < 13.5f) {
            bmi = 13.5f;
        } else if (bmi > 40.5f) {
            bmi = 40.5f;
        }
        updateStatus(bmi);
        txtThumb.setText(String.format(Locale.US, "%.01f", bmi));
        ((TextView) rootView.findViewById(R.id.txt_bmi_value)).setText(String.format(Locale.US, "%.01f", bmi));
        float percent = (bmi - 13.5f) / (40.5f - 13.5f);
        setPosition(percent);
    }

    private void updateStatus(float bmi) {
        String status;
        if (bmi < 18.5) {
            status = getResources().getString(R.string.under_weight);
        } else if (bmi >= 18.5 && bmi < 25) {
            status = getResources().getString(R.string.normal_weight);
        } else if (bmi >= 25 && bmi < 30) {
            status = getResources().getString(R.string.over_weight);
        } else {
            status = getResources().getString(R.string.obesity);
        }
        txtStatus.setText(status);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        View.OnClickListener onClickListener = view -> new BMIDialogFragment(this).show(getChildFragmentManager(), null);
        rootView.findViewById(R.id.btn_edit).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.txt_height_value).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btn_edit_height).setOnClickListener(onClickListener);
    }

    private void setPosition(float percent) {
        int widthImg = btnThumb.getWidth() / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnThumb.getLayoutParams();
        layoutParams.setMargins((int) (widthMax * percent) - widthImg, 0, 0, 0);
        btnThumb.setLayoutParams(layoutParams);

        int widthTxt = txtThumb.getWidth() / 2;
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_start);
        txtThumb.setX((int) (widthMax * percent) - widthTxt + padding);
    }

    @Override
    public void onResult(int type, Object value) {
        calBMI();
        updateHeight();
    }
}
