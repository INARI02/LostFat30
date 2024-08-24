package com.nhn.fitness.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Section;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.ui.activities.SectionDetailActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
    List<SectionUser> data;

    public TrainingAdapter() {
        data = new ArrayList<>();
    }

    public void setList(List<SectionUser> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_section, parent, false);
        return new TrainingAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        View btnOption;
        TextView textView ,tvExercises;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.txt_title);
            tvExercises = itemView.findViewById(R.id.txt_daily_exercises);
            btnOption = itemView.findViewById(R.id.btn_option);
            btnOption.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void bind(SectionUser item) {
            textView.setText(item.getData().getTitleDisplay());
            tvExercises.setText(item.getData().getNumberWorkoutsString(textView.getContext()).substring(0,1));
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_option) {
                showPopup(view);
            } else {
                SectionUser sectionUser = data.get(getAdapterPosition());
                Intent intent = new Intent(view.getContext(), SectionDetailActivity.class);
                intent.putExtra("com/nhn/fitness/data", sectionUser);
                view.getContext().startActivity(intent);
            }
        }

        private void showPopup(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view , Gravity.END, 0 , R.style.MyPopupMenu);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.training_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_rename) {
                rename();
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                delete();
                return true;
            }
            return false;
        }

        @SuppressLint("CheckResult")
        private void delete() {
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_delete_exercise);
            TextView btDialogDelete = (TextView) dialog.findViewById(R.id.tv_dialog_delete);
            btDialogDelete.setOnClickListener(view -> {
                SectionRepository.getInstance().delete(data.get(getAdapterPosition()).getData()).subscribe(() -> {
                    SectionRepository.getInstance().delete(data.get(getAdapterPosition())).subscribe();
                });
                dialog.dismiss();
            });
            TextView btDialogCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
            btDialogCancel.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        @SuppressLint("CheckResult")
        private void rename() {
            Section section = data.get(getAdapterPosition()).getData();
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.enter_name_training);
            AppCompatEditText editText = dialog.findViewById(R.id.edt_name);
            editText.setText(section.getTitle());
            TextView btDialogAddName = (TextView) dialog.findViewById(R.id.tv_dialog_add_name);
            btDialogAddName.setOnClickListener(view -> {
                String txtValue = editText.getText().toString();
                if (!TextUtils.isEmpty(txtValue)) {
                    section.setTitle(editText.getText().toString());
                    SectionRepository.getInstance().updateSection(section).subscribe(() -> {
                        SectionUser sectionUser = data.get(getAdapterPosition());
                        sectionUser.setUpdated(Calendar.getInstance().getTime());
                        SectionRepository.getInstance().updateSectionUser(sectionUser).subscribe();
                        dialog.dismiss();
                    });
                } else {
                    Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.required_missing), Toast.LENGTH_LONG).show();
                }
            });
            TextView btDialogCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
            btDialogCancel.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}
