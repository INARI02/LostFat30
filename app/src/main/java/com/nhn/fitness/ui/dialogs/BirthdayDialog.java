package com.nhn.fitness.ui.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthdayDialog extends BaseDialog implements DatePicker.OnDateChangedListener {
    private DialogResultListener listener;
    private DatePicker datePicker;
    private long birthday = -1;
    private boolean isSave = false;
    private com.nhn.fitness.ui.interfaces.Callback callback;

    public BirthdayDialog(DialogResultListener listener) {
        this.hasTitle = false;
        this.listener = listener;
    }

    public void setCallback(com.nhn.fitness.ui.interfaces.Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_birthday, container, false);
        initViews();
        initData();
        initEvents();
        initObserve();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(AppSettings.getInstance().getBirthday());
        datePicker = rootView.findViewById(R.id.date_picker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> save());
    }

    private void save() {
        if (birthday != -1) {
            UserDTO temp = SessionManager.getInstance().getCurrentUser().clone();
            temp.setBirthdate(new Date(birthday));
            // Save ngay sinh len server
            RestApiHelper.getInstance().editUserInfo(temp, new Callback<UserDTO>() {
                @Override
                public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        AppSettings.getInstance().setBirthday(birthday);
                        isSave = true;
                        if (callback != null) {
                            callback.execute(birthday);
                        }
                    } else {
                        // Show error
                        Toast.makeText(requireContext(), "err: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable throwable) {
                    // Show error
                    Toast.makeText(requireContext(), "err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isSave && listener != null) {
            listener.onResult(-1, null);
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        birthday = calendar.getTimeInMillis();//DateUtils.getIdDay(calendar);
    }
}
