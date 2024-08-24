package com.nhn.fitness.ui.dialogs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.customViews.HeightUnitSwitchView;
import com.nhn.fitness.ui.customViews.WeightUnitSwitchView;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.ui.interfaces.UnitTypeChangeListener;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BMIDialogFragment extends BaseDialog implements UnitTypeChangeListener {

    private DialogResultListener listener;
    private boolean isSave = false;
    private EditText edtHeight, edtWeight;
    private WeightUnitSwitchView weightUnitSwitchView;
    private HeightUnitSwitchView heightUnitSwitchView;
    private int type;
    private DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private DecimalFormat df = new DecimalFormat("#.##", symbols);
    private DecimalFormat df2 = new DecimalFormat("#", symbols);

    public BMIDialogFragment(DialogResultListener listener) {
        this.hasTitle = false;
        this.listener = listener;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bmidialog, container, false);
        initViews();
        initData();
        initEvents();
        initObserve();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        df.setRoundingMode(RoundingMode.HALF_UP);
        df2.setRoundingMode(RoundingMode.HALF_UP);
        type = AppSettings.getInstance().getUnitType();
        edtHeight = rootView.findViewById(R.id.edt_height);
        edtWeight = rootView.findViewById(R.id.edt_weight);

        if (type == 1) {
            edtHeight.setText(df.format(Utils.convertCmToFt(AppSettings.getInstance().getHeightDefault())));
            edtWeight.setText(df.format(Utils.convertKgToLbs(AppSettings.getInstance().getWeightDefault())));
        } else {
            edtHeight.setText(df2.format(AppSettings.getInstance().getHeightDefault()));
            edtWeight.setText(df.format(AppSettings.getInstance().getWeightDefault()));
        }

        heightUnitSwitchView = rootView.findViewById(R.id.sw_height);
        weightUnitSwitchView = rootView.findViewById(R.id.sw_weight);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> {
            String txtW = edtWeight.getText().toString();
            String txtH = edtHeight.getText().toString();
            if (!TextUtils.isEmpty(txtW) && !TextUtils.isEmpty(txtH)) {
                saveData();
            } else {
                Toast.makeText(getActivity(), getString(R.string.required_missing), Toast.LENGTH_LONG).show();
            }
        });

        heightUnitSwitchView.setListener(this);
        weightUnitSwitchView.setListener(this);
    }

    private void refreshValue() {
        float weight, height;
        if (type == 1) {
            weight = Utils.convertKgToLbs(Float.parseFloat(edtWeight.getText().toString()));
            height = Utils.convertCmToFt(Float.parseFloat(edtHeight.getText().toString()));
        } else {
            weight = Utils.convertLbsToKg(Float.parseFloat(edtWeight.getText().toString()));
            height = Utils.convertFtToCm(Float.parseFloat(edtHeight.getText().toString()));
        }

        edtHeight.setText(type == 0 ? df2.format(height) : df.format(height));
        edtWeight.setText(df.format(weight));
    }

    private void saveData() {
        isSave = true;
        float weight = Float.parseFloat(edtWeight.getText().toString());
        float height = Float.parseFloat(edtHeight.getText().toString());
        if (type == 1) {
            weight = Utils.convertLbsToKg(weight);
            height = Utils.convertFtToCm(height);
        }
        AppSettings.getInstance().setWeightDefault(weight);
        AppSettings.getInstance().setHeightDefault(height);
        saveDayHistory();
        dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isSave) {
            listener.onResult(DialogResultListener.HEIGHT, null);
        }
    }

    @Override
    public void onChange(int unit) {
        if (unit != type) {
            type = unit;
            heightUnitSwitchView.setTypeUnit(unit);
            weightUnitSwitchView.setTypeUnit(unit);
            refreshValue();
        }
    }

    private void saveDayHistory() {
        float weight = Float.parseFloat(edtWeight.getText().toString());
        DayHistoryModel dayHistoryModel = DayHistoryRepository.getInstance().getByIdWithoutObserve(DateUtils.getIdNow());
        if (dayHistoryModel == null) {
            dayHistoryModel = new DayHistoryModel((Calendar) Calendar.getInstance().clone());
            dayHistoryModel.setWeight(weight);
            addDisposable(DayHistoryRepository.getInstance().insert(dayHistoryModel).subscribe());
        } else {
            dayHistoryModel.setWeight(weight);
            addDisposable(DayHistoryRepository.getInstance().update(dayHistoryModel).subscribe());
        }
    }
}
