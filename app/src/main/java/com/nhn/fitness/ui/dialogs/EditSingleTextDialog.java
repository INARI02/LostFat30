package com.nhn.fitness.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.interfaces.Callback;

public class EditSingleTextDialog extends BaseDialog {
    public static final String TAG = EditSingleTextDialog.class.getSimpleName();
    private EditText etText;
    private String textValue;
    private Callback callback;

    public EditSingleTextDialog(String initialText, Callback callback) {
        this.hasTitle = false;
        this.textValue = initialText;
        this.callback = callback;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_edit_single_text, container, false);
        initViews();
        initData();
        initEvents();
        initObserve();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        etText = rootView.findViewById(R.id.et_single_text);
    }

    @Override
    protected void initData() {
        super.initData();
        etText.setText(textValue);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> {
            String text = etText.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(getActivity(), "Không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            textValue = text;
            if (callback != null) {
                callback.execute(textValue);
            }
            dismissAllowingStateLoss();
        });
    }
}
