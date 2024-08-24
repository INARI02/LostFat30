package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.nhn.fitness.data.room.StringListConverters;

import java.util.List;

@Entity(tableName = "training")
public class Training implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    @TypeConverters(StringListConverters.class)
    private List<String> workoutsUserId;
    @Ignore
    private List<WorkoutUser> data;

    @Ignore
    public Training() {
    }

    public Training(@NonNull String id, String name, List<String> workoutsUserId) {
        this.id = id;
        this.name = name;
        this.workoutsUserId = workoutsUserId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWorkoutsUserId() {
        return workoutsUserId;
    }

    public void setWorkoutsUserId(List<String> workoutsUserId) {
        this.workoutsUserId = workoutsUserId;
    }

    public List<WorkoutUser> getData() {
        return data;
    }

    public void setData(List<WorkoutUser> data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeStringList(this.workoutsUserId);
        dest.writeTypedList(this.data);
    }

    protected Training(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.workoutsUserId = in.createStringArrayList();
        this.data = in.createTypedArrayList(WorkoutUser.CREATOR);
    }

    public static final Creator<Training> CREATOR = new Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel source) {
            return new Training(source);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };
}
