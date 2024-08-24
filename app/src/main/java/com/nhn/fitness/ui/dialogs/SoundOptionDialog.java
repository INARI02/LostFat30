package com.nhn.fitness.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.MessageEvent;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;

import org.greenrobot.eventbus.EventBus;

public class SoundOptionDialog extends BaseDialog implements CompoundButton.OnCheckedChangeListener {

    public SoundOptionDialog() {
        this.hasTitle = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_sound_option, container, false);
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        refreshView();
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        addListener();
        rootView.findViewById(R.id.btn_ok).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.tv_dialog_cancel).setOnClickListener(view -> {
            dismiss();
        });
    }

    private void addListener() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setOnCheckedChangeListener(this);
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setOnCheckedChangeListener(this);
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setOnCheckedChangeListener(this);
    }

    private void removeListener() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setOnCheckedChangeListener(null);
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setOnCheckedChangeListener(null);
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setOnCheckedChangeListener(null);
    }

    private void refreshView() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setChecked(!AppSettings.getInstance().getSound());
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(AppSettings.getInstance().getSoundVoice());
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(AppSettings.getInstance().getSoundCoachTips());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_mute:
                AppSettings.getInstance().setSound(!b);
                removeListener();
                if (b) {
                    ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(false);
                    ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(false);
                    rootView.findViewById(R.id.sw_voice).setEnabled(false);
                    rootView.findViewById(R.id.sw_coach).setEnabled(false);
                } else {
                    ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(AppSettings.getInstance().getSoundVoice());
                    ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(AppSettings.getInstance().getSoundCoachTips());
                    rootView.findViewById(R.id.sw_voice).setEnabled(true);
                    rootView.findViewById(R.id.sw_coach).setEnabled(true);
                }
                addListener();
                if (b) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.STOP_AUDIO, null));
                }
                break;
            case R.id.sw_voice:
                AppSettings.getInstance().setSoundVoice(b);
                if (!b) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.STOP_AUDIO, null));
                }
                break;
            case R.id.sw_coach:
                AppSettings.getInstance().setSoundCoachTips(b);
//                if (!b) {
//                EventBus.getDefault().post(new MessageEvent(MessageEvent.STOP_AUDIO, null));
//                }
                break;
        }
    }
}
