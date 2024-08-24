package com.nhn.fitness.ui.dialogs;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.customViews.WeightUnitSwitchView;
import com.nhn.fitness.ui.dialogs.viewmodel.WeightDialogViewModel;
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

public class WeightDialogFragment extends BaseDialog implements HorizontalCalendarListener, UnitTypeChangeListener {
    private WeightDialogViewModel viewModel;
    private DialogResultListener listener;
    private DayHistoryModel dayHistoryModel;
    private DateModel currentDateSelected;
    private EditText edtInput;
    private int unitType;
    private boolean isNew = true;
    private boolean hasUpdate = false;
    private boolean newData = false;

    public WeightDialogFragment(DialogResultListener listener) {
        this.listener = listener;
        this.hasTitle = false;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_weight_dialog, container, false);
        viewModel = new WeightDialogViewModel();
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

        WeightUnitSwitchView unitSwitchView = rootView.findViewById(R.id.sw_unit);
        unitSwitchView.setListener(this);
    }

    @Override
    protected void initObserve() {
        super.initObserve();
        viewModel.unitType.observe(getViewLifecycleOwner(), integer -> {
            if (unitType != integer) {
                hasUpdate = true;
                if (integer == 0) {
                    viewModel.weight.setValue(Utils.convertLbsToKg(viewModel.weight.getValue()));
                } else if (integer == 1) {
                    viewModel.weight.setValue(Utils.convertKgToLbs(viewModel.weight.getValue()));
                }
//                Log.e("status", "typeChange: " + unitType + " current: " + unitType);
                unitType = integer;
            }
        });
        viewModel.weight.observe(getViewLifecycleOwner(), value -> {
//            Log.e("status", "setText: " + value);
            edtInput.setText(Utils.formatWeight(value));
        });
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> {
            saveData();
        });
    }

    private void saveData() {
        float value = Float.parseFloat(edtInput.getText().toString());
        if (value > 0 && value < (unitType == 0 ? 500 : 1100)) {
            hasUpdate = true;
            newData = true;
            if (unitType == 1) {
                value = Utils.convertLbsToKg(value);
            }
            if (isNew) {
                dayHistoryModel = new DayHistoryModel(currentDateSelected.getCalendar());
                dayHistoryModel.setWeight(value);
                addDisposable(DayHistoryRepository.getInstance()
                        .insert(dayHistoryModel).subscribe(this::dismissAllowingStateLoss));
            } else {
                dayHistoryModel.setWeight(value);
                addDisposable(DayHistoryRepository.getInstance()
                        .update(dayHistoryModel).subscribe(this::dismissAllowingStateLoss));
            }
            saveNewestWeight();
            AppSettings.getInstance().setWeightDefault(value);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @SuppressLint("CheckResult")
    private void saveNewestWeight() {
        DayHistoryRepository.getInstance().getWeightNewest()
                .observeOn(Schedulers.io())
                .subscribe(response -> AppSettings.getInstance().setWeightDefault(response.getWeight()),
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
                            if (response.getWeight() > 0) {
//                                Log.e("status", "have at current");
                                setWeightByKg(response.getWeight());
                            } else {
                                addDisposable(
                                        Single.just(selectedDate.getDayCount())
                                                .flatMap((Function<Long, Single<Float>>) aLong -> Single.just(findAround(aLong)))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(weight -> {
                                                    setWeightByKg(weight);
                                                })
                                );
                            }
                        }, error -> {
//                            // count == 0
//
//                            Log.e("status", "response error");
                            isNew = true;
//                            dayHistoryModel = new DayHistoryModel(selectedDate.getCalendar());
//                            setWeightByKg(AppSettings.getInstance().getWeightDefault());
                            addDisposable(
                                    Single.just(selectedDate.getDayCount())
                                            .flatMap((Function<Long, Single<Float>>) aLong -> Single.just(findAround(aLong)))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(weight -> {
                                                setWeightByKg(weight);
                                            })
                            );
                        }
                ));
    }

    private float findAround(long id) {
        ArrayList<DayHistoryModel> before = new ArrayList<>(AppDatabase.getInstance().dayHistoryDao().findWeightBefore(id));
        if (before.size() == 0) {
            ArrayList<DayHistoryModel> after = new ArrayList<>(AppDatabase.getInstance().dayHistoryDao().findWeightAfter(id));
            if (after.size() == 0) {
//                Log.e("status", "table null");
                return AppSettings.getInstance().getWeightDefault();
            } else {
//                Log.e("status", "after: " + after.size());
                return after.get(0).getWeight();
            }
        } else {
//            Log.e("status", "before: " + before.size());
            return before.get(0).getWeight();
        }
    }

    @Override
    public void onChange(int unit) {
//        Log.e("status", "unitChangeBy SwitchView: " + unit);
        viewModel.unitType.setValue(unit);
    }

    private void setWeightByKg(float value) {
//        Log.e("status", "setWeightByKg: " + value + " type: " + unitType);
        if (unitType == 1) {
            viewModel.weight.setValue(Utils.convertKgToLbs(value));
        } else {
            viewModel.weight.setValue(value);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (hasUpdate && listener != null) {
            listener.onResult(DialogResultListener.WEIGHT, newData);
        }
    }
}
