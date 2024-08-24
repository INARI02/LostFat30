package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "challenge")
public class ChallengeDay implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private int week;
    private int day;
    private String sectionId;
    private int status;
    @Ignore
    private SectionUser data;

    public ChallengeDay(String id, int week, int day, String sectionId, int status) {
        this.id = id;
        this.week = week;
        this.day = day;
        this.sectionId = sectionId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        dest.writeString(this.id);
        dest.writeInt(this.week);
        dest.writeInt(this.day);
        dest.writeString(this.sectionId);
        dest.writeInt(this.status);
        dest.writeParcelable(this.data, flags);
    }

    protected ChallengeDay(Parcel in) {
        this.id = in.readString();
        this.week = in.readInt();
        this.day = in.readInt();
        this.sectionId = in.readString();
        this.status = in.readInt();
        this.data = in.readParcelable(SectionUser.class.getClassLoader());
    }

    public static final Creator<ChallengeDay> CREATOR = new Creator<ChallengeDay>() {
        @Override
        public ChallengeDay createFromParcel(Parcel source) {
            return new ChallengeDay(source);
        }

        @Override
        public ChallengeDay[] newArray(int size) {
            return new ChallengeDay[size];
        }
    };
}
