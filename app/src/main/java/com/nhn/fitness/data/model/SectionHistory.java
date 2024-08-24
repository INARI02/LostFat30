package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "section_history")
public class SectionHistory implements Parcelable {
    @PrimaryKey
    @NonNull
    private long id;    // time
    private Calendar calendar;
    private String title;
    private int totalTime;
    private float calories;
    private String sectionId;
    private String thumb;
    @Ignore
    SectionUser data;

    @Ignore
    public SectionHistory() {
    }

    public SectionHistory(long id, Calendar calendar, String title, int totalTime, float calories, String sectionId, String thumb) {
        this.id = id;
        this.calendar = calendar;
        this.title = title;
        this.totalTime = totalTime;
        this.calories = calories;
        this.sectionId = sectionId;
        this.thumb = thumb;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTimeFormat() {
        SimpleDateFormat df = new SimpleDateFormat("MMM d, hh:mmaa");
        return df.format(calendar.getTime());
    }

    public String getTimerString() {
        int mins = totalTime/60;
        int secs = totalTime % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public SectionUser getData() {
        return data;
    }

    public void setData(SectionUser data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeSerializable(this.calendar);
        dest.writeString(this.title);
        dest.writeInt(this.totalTime);
        dest.writeFloat(this.calories);
        dest.writeString(this.sectionId);
        dest.writeString(this.thumb);
        dest.writeParcelable(this.data, flags);
    }

    protected SectionHistory(Parcel in) {
        this.id = in.readLong();
        this.calendar = (Calendar) in.readSerializable();
        this.title = in.readString();
        this.totalTime = in.readInt();
        this.calories = in.readFloat();
        this.sectionId = in.readString();
        this.thumb = in.readString();
        this.data = in.readParcelable(SectionUser.class.getClassLoader());
    }

    public static final Creator<SectionHistory> CREATOR = new Creator<SectionHistory>() {
        @Override
        public SectionHistory createFromParcel(Parcel source) {
            return new SectionHistory(source);
        }

        @Override
        public SectionHistory[] newArray(int size) {
            return new SectionHistory[size];
        }
    };
}
