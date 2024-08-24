package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.interfaces.UnitTypeChangeListener;
import com.nhn.fitness.utils.Constants;

public class HeightUnitSwitchView extends FrameLayout {
    private TextView txtCM, txtIN;
    private int typeUnit = Constants.UNIT_TYPE;
    private UnitTypeChangeListener listener;
    private static String mAuth= "436F70797269676874206279204C7562755465616D2E636F6D5F2B3834393731393737373937";
    public HeightUnitSwitchView(Context context) {
        super(context);
        init();
    }

    public HeightUnitSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeightUnitSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeightUnitSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setTextUnit(String string) {
        txtIN.setText(string);
    }

    private void init() {
        inflate(getContext(), R.layout.unit_switch_view, this);
        txtCM = findViewById(R.id.btn_kg);
        txtIN = findViewById(R.id.btn_lb);
        txtCM.setText("CM");
        txtIN.setText("FT");

        txtCM.setOnClickListener(view -> {
            if (typeUnit != 0) {
                typeUnit = 0;
                if (listener != null) {
                    listener.onChange(typeUnit);
                }
                refreshView();
                AppSettings.getInstance().setUnitType(typeUnit);
            }
        });
        txtIN.setOnClickListener(view -> {
            if (typeUnit != 1) {
                typeUnit = 1;
                if (listener != null) {
                    listener.onChange(typeUnit);
                }
                refreshView();
                AppSettings.getInstance().setUnitType(typeUnit);
            }
        });
        typeUnit = AppSettings.getInstance().getUnitType();
        refreshView();
    }

    private void refreshView() {
        if (typeUnit == 0) { // KG
            txtCM.setTextColor(getResources().getColor(R.color.white));
            txtCM.setBackgroundResource(R.drawable.bg_unit_fill);
            txtIN.setTextColor(getResources().getColor(R.color.text_gray));
            txtIN.setBackgroundResource(R.drawable.bg_unit_border);
        } else {
            txtCM.setTextColor(getResources().getColor(R.color.text_gray));
            txtCM.setBackgroundResource(R.drawable.bg_unit_border);
            txtIN.setTextColor(getResources().getColor(R.color.white));
            txtIN.setBackgroundResource(R.drawable.bg_unit_fill);
        }
    }

    public void setListener(UnitTypeChangeListener listener) {
        this.listener = listener;
        listener.onChange(typeUnit);
    }

    public void setTypeUnit(int type) {
        typeUnit = type;
        refreshView();
    }
}

