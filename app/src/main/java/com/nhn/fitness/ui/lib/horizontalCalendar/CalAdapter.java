package com.nhn.fitness.ui.lib.horizontalCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CalAdapter extends RecyclerView.Adapter<CalAdapter.MyViewHolder> {

    private Context context;
    private HorizontalCalendarListener listener;
    private DateModel current = new DateModel(Calendar.getInstance());
    private int currentPosition;

    private ArrayList<DateModel> list;

    public CalAdapter(Context context, ArrayList<DateModel> list, int currentPosition) {
        this.list = list;
        this.context = context;
        this.currentPosition = currentPosition;
    }

    public void setListener(HorizontalCalendarListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView day, date;

        MyViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
            date = view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }

        void bind(int pos) {
            DateModel item = list.get(pos);
            day.setText(item.getDay());
            date.setText(item.getDate());
            itemView.setTag(pos);
            if (!item.isEnable()) {
                date.setTextColor(context.getResources().getColor(R.color.text_gray_light));
                day.setTextColor(context.getResources().getColor(R.color.text_gray_light));
            } else if (item.equals(current)) {
                date.setTextColor(context.getResources().getColor(R.color.blueLight));
                day.setTextColor(context.getResources().getColor(R.color.blueLight));
            } else if (item.isToday()) {
                date.setTextColor(context.getResources().getColor(R.color.color_today));
                day.setTextColor(context.getResources().getColor(R.color.color_today));
            } else {
                date.setTextColor(context.getResources().getColor(R.color.text_gray));
                day.setTextColor(context.getResources().getColor(R.color.text_gray));
            }
        }

        @Override
        public void onClick(View view) {
            handleClick(getAdapterPosition());
        }
    }

    public void handleClick(int pos) {
        DateModel dateModel = list.get(pos);
        if (dateModel.isEnable()) {
            listener.newDateSelected(dateModel, pos);
            int lastPosition = currentPosition;
            currentPosition = pos;
            current = list.get(pos);
            notifyItemChanged(lastPosition);
            notifyItemChanged(currentPosition);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public CalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_day_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.bind(position);
    }

}
