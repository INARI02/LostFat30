package com.nhn.fitness.ui.dialogs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.customViews.HeightUnitSwitchView;
import com.nhn.fitness.ui.dialogs.viewmodel.WaistlineDialogViewModel;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.ui.interfaces.UnitTypeChangeListener;
import com.nhn.fitness.ui.lib.horizontalCalendar.DateModel;
import com.nhn.fitness.ui.lib.horizontalCalendar.HorizontalCalendarListener;
import com.nhn.fitness.ui.lib.horizontalCalendar.HorizontalCalendarView;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WaistlineDialogFragment extends BaseDialog implements HorizontalCalendarListener, UnitTypeChangeListener {
    private WaistlineDialogViewModel viewModel;
    private DialogResultListener listener;
    private DayHistoryModel dayHistoryModel;
    private DateModel currentDateSelected;
    private EditText edtInput;
    private int unitType;
    private boolean isNew = true;
    private boolean hasUpdate = false;
    private boolean newData = false;

    public WaistlineDialogFragment(DialogResultListener listener) {
        this.listener = listener;
        this.hasTitle = false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_waistline_dialog, container, false);
        viewModel = new WaistlineDialogViewModel();
        initViews();
        initObserve();
        initEvents();
        initCalendar();
        return rootView;
    }

    private void initCalendar() {
        HorizontalCalendarView calendarView = rootView.findViewById(R.id.calender);
        calendarView.setListener(this);
        calendarView.init();
    }

    @Override
    protected void initViews() {
        super.initViews();
        unitType = AppSettings.getInstance().getUnitType();

        edtInput = rootView.findViewById(R.id.edt_input);

        HeightUnitSwitchView unitSwitchView = rootView.findViewById(R.id.sw_unit);
        unitSwitchView.setTextUnit("INCH");
        unitSwitchView.setListener(this);
    }

    @Override
    protected void initObserve() {
        super.initObserve();
        viewModel.unitType.observe(getViewLifecycleOwner(), integer -> {
            if (unitType != integer) {
                hasUpdate = true;
                if (integer == 0) {
                    viewModel.waistline.setValue(Utils.convertInchToCm(viewModel.waistline.getValue()));
                } else if (integer == 1) {
                    viewModel.waistline.setValue(Utils.convertCmToInch(viewModel.waistline.getValue()));
                }
//                Log.e("status", "typeChange: " + unitType + " current: " + unitType);
                unitType = integer;
            }
        });
        viewModel.waistline.observe(getViewLifecycleOwner(), value -> {
//            Log.e("status", "setText: " + value);
            edtInput.setText(Utils.formatWaistline(value));
        });
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> {
            String txt = edtInput.getText().toString();
            if (!TextUtils.isEmpty(txt)) {
                saveData();
            } else {
                Toast.makeText(getActivity(), getString(R.string.required_missing), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveData() {
        float value = Float.parseFloat(edtInput.getText().toString());
        if (value > 0 && value < (unitType == 0 ? 500 : 1100)) {
            hasUpdate = true;
            newData = true;
            if (unitType == 1) {
                value = Utils.convertInchToCm(value);
            }
            if (isNew) {
                dayHistoryModel = new DayHistoryModel(currentDateSelected.getCalendar());
                dayHistoryModel.setWaistline(value);
                addDisposable(DayHistoryRepository.getInstance()
                        .insert(dayHistoryModel).subscribe(this::dismissAllowingStateLoss));
            } else {
                dayHistoryModel.setWaistline(value);
                addDisposable(DayHistoryRepository.getInstance()
                        .update(dayHistoryModel).subscribe(this::dismissAllowingStateLoss));
            }
            saveNewestWaistline();
            AppSettings.getInstance().setWaistlineDefault(value);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @SuppressLint("CheckResult")
    private void saveNewestWaistline() {
        DayHistoryRepository.getInstance().getWaistlineNewest()
                .observeOn(Schedulers.io())
                .subscribe(response -> AppSettings.getInstance().setWaistlineDefault(response.getWaistline()),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void updateMonthOnScroll(DateModel selectedDate) {

    }

    @Override
    public void newDateSelected(DateModel selectedDate, int pos) {
//        Log.e("status", "date click: " + selectedDate.getDayCount());
        currentDateSelected = selectedDate;
        addDisposable(DayHistoryRepository.getInstance()
                .getById(selectedDate.getDayCount())
                .subscribe(response -> {
                            isNew = false;
                            dayHistoryModel = response;
                            if (response.getWaistline() > 0) {
//                                Log.e("status", "have at current");
                                setWaistlineByCm(response.getWaistline());
                            } else {
                                addDisposable(
                                        Single.just(selectedDate.getDayCount())
                                                .flatMap((Function<Long, Single<Float>>) aLong -> Single.just(findAround(aLong)))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(waistline -> {
                                                    setWaistlineByCm(waistline);
                                                })
                                );
                            }
                        }, error -> {
//                            // count == 0
//
//                            Log.e("status", "response error");
                            isNew = true;
//                            dayHistoryModel = new DayHistoryModel(selectedDate.getCalendar());
//                            setWaistlineByCm(AppSettings.getInstance().getWeightDefault());
                            addDisposable(
                                    Single.just(selectedDate.getDayCount())
                                            .flatMap((Function<Long, Single<Float>>) aLong -> Single.just(findAround(aLong)))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(waistline -> {
                                                setWaistlineByCm(waistline);
                                            })
                            );
                        }
                ));
    }

    private float findAround(long id) {
        ArrayList<DayHistoryModel> before = new ArrayList<>(AppDatabase.getInstance().dayHistoryDao().findWaistlineBefore(id));
        if (before.size() == 0) {
            ArrayList<DayHistoryModel> after = new ArrayList<>(AppDatabase.getInstance().dayHistoryDao().findWaistlineAfter(id));
            if (after.size() == 0) {
//                Log.e("status", "table null");
                return AppSettings.getInstance().getWaistlineDefault();
            } else {
//                Log.e("status", "after: " + after.size());
                return after.get(0).getWaistline();
            }
        } else {
//            Log.e("status", "before: " + before.size());
            return before.get(0).getWaistline();
        }
    }

    @Override
    public void onChange(int unit) {
//        Log.e("status", "unitChangeBy SwitchView: " + unit);
        viewModel.unitType.setValue(unit);
    }

    private void setWaistlineByCm(float value) {
//        Log.e("status", "setWaistlineByCm: " + value + " type: " + unitType);
        if (unitType == 1) {
            viewModel.waistline.setValue(Utils.convertCmToInch(value));
        } else {
            viewModel.waistline.setValue(value);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (hasUpdate && listener != null) {
            listener.onResult(DialogResultListener.WAISTLINE, newData);
        }
    }
}
