package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_section")
public class DailySection implements Parcelable {
    @PrimaryKey
    @NonNull
    private int id;
    @ColumnInfo(name = "section_id")
    private String sectionId;
    private int level;
    @ColumnInfo(name = "is_rest_day")
    private boolean isRestDay;
    private int day;
    @Ignore
    SectionUser data;

    public DailySection(int id, String sectionId, int level, boolean isRestDay, int day) {
        this.id = id;
        this.sectionId = sectionId;
        this.level = level;
        this.isRestDay = isRestDay;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isRestDay() {
        return isRestDay;
    }

    public void setRestDay(boolean restDay) {
        isRestDay = restDay;
    }

    public SectionUser getData() {
        return data;
    }

    public void setData(SectionUser data) {
        this.data = data;
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
        dest.writeString(this.sectionId);
        dest.writeInt(this.level);
        dest.writeByte(this.isRestDay ? (byte) 1 : (byte) 0);
        dest.writeInt(this.day);
        dest.writeParcelable(this.data, flags);
    }

    protected DailySection(Parcel in) {
        this.id = in.readInt();
        this.sectionId = in.readString();
        this.level = in.readInt();
        this.isRestDay = in.readByte() != 0;
        this.day = in.readInt();
        this.data = in.readParcelable(SectionUser.class.getClassLoader());
    }

    public static final Creator<DailySection> CREATOR = new Creator<DailySection>() {
        @Override
        public DailySection createFromParcel(Parcel source) {
            return new DailySection(source);
        }

        @Override
        public DailySection[] newArray(int size) {
            return new DailySection[size];
        }
    };
}
