package com.nhn.fitness.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_section_user")
public class DailySectionUser implements Parcelable {
    @PrimaryKey
    @NonNull
    private int id;
    private int level;
    private int day;
    private float progress;
    private boolean locked;
    private boolean isRestDay;
    private boolean completed;
    @Ignore
    private DailySection data;

    @Ignore
    public DailySectionUser() {
    }

    public DailySectionUser(int id, int level, float progress, boolean locked, boolean isRestDay, boolean completed, int day) {
        this.id = id;
        this.level = level;
        this.progress = progress;
        this.locked = locked;
        this.isRestDay = isRestDay;
        this.completed = completed;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public DailySection getData() {
        return data;
    }

    public void setData(DailySection data) {
        this.data = data;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isRestDay() {
        return isRestDay;
    }

    public void setRestDay(boolean restDay) {
        isRestDay = restDay;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.level);
        dest.writeInt(this.day);
        dest.writeFloat(this.progress);
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRestDay ? (byte) 1 : (byte) 0);
        dest.writeByte(this.completed ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.data, flags);
    }

    protected DailySectionUser(Parcel in) {
        this.id = in.readInt();
        this.level = in.readInt();
        this.day = in.readInt();
        this.progress = in.readFloat();
        this.locked = in.readByte() != 0;
        this.isRestDay = in.readByte() != 0;
        this.completed = in.readByte() != 0;
        this.data = in.readParcelable(DailySection.class.getClassLoader());
    }

    public static final Creator<DailySectionUser> CREATOR = new Creator<DailySectionUser>() {
        @Override
        public DailySectionUser createFromParcel(Parcel source) {
            return new DailySectionUser(source);
        }

        @Override
        public DailySectionUser[] newArray(int size) {
            return new DailySectionUser[size];
        }
    };
}

