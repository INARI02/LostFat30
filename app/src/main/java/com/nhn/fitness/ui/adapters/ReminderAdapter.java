package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.ui.dialogs.ReminderRepeatPicker;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private FragmentManager fragmentManager;
    private ArrayList<Reminder> reminders;


    public ReminderAdapter(FragmentManager fragmentManager, ArrayList<Reminder> list) {
        this.fragmentManager = fragmentManager;
        this.reminders = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_row_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reminders.get(position));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {
        private TextView txtTime, txtTitle, txtRepeat, txtRepeatTitle;
        private SwitchCompat swEnable;
        private View btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtRepeat = itemView.findViewById(R.id.txt_repeat);
            txtRepeatTitle = itemView.findViewById(R.id.txt_repeat_title);
            swEnable = itemView.findViewById(R.id.sw_enable);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(this);
            txtRepeat.setOnClickListener(this);
            txtRepeatTitle.setOnClickListener(this);
            txtTitle.setOnClickListener(this);
            txtTime.setOnClickListener(this);
        }

        void bind(Reminder reminder) {
            txtTime.setText(String.format("%02d:%02d", reminder.getHours(), reminder.getMins()));
            if (reminder.isAdmin()) {
                btnDelete.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText(reminder.getTitle());
            } else {
                txtTitle.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
            txtTitle.setText(reminder.getTitle());
            txtRepeat.setText(reminder.getRepeatsString(txtRepeat.getContext()));
            swEnable.setChecked(reminder.isEnable());
            swEnable.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            Reminder reminder = reminders.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.txt_time:
                    setTime(view.getContext(), reminder);
                    break;
                case R.id.txt_repeat_title:
                case R.id.txt_repeat:
                    if (!reminder.isAdmin()) {
                        new ReminderRepeatPicker(reminder).show(fragmentManager, null);
                    }
                    break;
                case R.id.txt_title:
                    if (!reminder.isAdmin()) {
                       showDialogRenameReminder();
                    }
                    break;
                case R.id.btn_delete:
                    if (!reminder.isAdmin()) {
                        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                                .setTitle(view.getResources().getString(R.string.tips))
                                .setMessage(view.getContext().getResources().getString(R.string.confirm_delete))
                                .setNegativeButton(view.getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setPositiveButton(view.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ReminderRepository.getInstance().delete(reminder).subscribe();
                                        dialogInterface.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                    break;
            }
        }

        @SuppressLint("CheckResult")
        private void showDialogRenameReminder() {
            Dialog dialog = new Dialog(itemView.getContext() ,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_rename_reminder);
            EditText edtTitle = dialog.findViewById(R.id.edt_remind_name);
            TextView btOk = (TextView) dialog.findViewById(R.id.btn_ok);
            btOk.setOnClickListener(view -> {
                Reminder reminder = reminders.get(getAdapterPosition());
                reminder.setTitle(edtTitle.getText().toString());
                ReminderRepository.getInstance().update(reminder).subscribe();
                dialog.dismiss();
            });
            TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        private void setTime(Context context, Reminder reminder) {
            // Show TimePicker
            Dialog dialog = new TimePickerDialog(context, this, reminder.getHours(), reminder.getMins(), true);
            dialog.show();
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            Reminder reminder = reminders.get(getAdapterPosition());
            reminder.setHours(i);
            reminder.setMins(i1);
            reminder.setAlarm(timePicker.getContext());
            ReminderRepository.getInstance().update(reminder).subscribe();
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Reminder reminder = reminders.get(getAdapterPosition());
            if (b) {
                reminder.setAlarm(compoundButton.getContext());
            } else {
                reminder.cancelAlarm(compoundButton.getContext());
            }
            reminder.setEnable(b);
            ReminderRepository.getInstance().update(reminder).subscribe();
        }
    }


}
