package com.nhn.fitness.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.utils.Utils;

import java.util.Locale;

@Entity(tableName = "workout_user")
public class WorkoutUser implements Parcelable {

    @PrimaryKey
    @NonNull
    private String workoutUserId;
    private String id;
    private int time = -1;
    private int count = -1;
    private int status = 0;  // 0: open, 1: finished
    private float calories = -1;
    private boolean isReplaced = false;
    private boolean isTraining = true;
    @Ignore
    private Workout data;

    @Ignore
    public WorkoutUser() {
    }

    // For replace function
    @Ignore
    public WorkoutUser(@NonNull String id) {
        this.id = id;
        this.workoutUserId = id;
    }

    public WorkoutUser(@NonNull String workoutUserId, String id, int time, int count, int status, float calories, boolean isReplaced, boolean isTraining) {
        this.workoutUserId = workoutUserId;
        this.id = id;
        this.time = time;
        this.count = count;
        this.status = status;
        this.calories = calories;
        this.isReplaced = isReplaced;
        this.isTraining = isTraining;
    }

    @NonNull
    public String getWorkoutUserId() {
        return workoutUserId;
    }

    public void setWorkoutUserId(@NonNull String workoutUserId) {
        this.workoutUserId = workoutUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isReplaced() {
        return isReplaced;
    }

    public void setReplaced(boolean replaced) {
        isReplaced = replaced;
    }

    public float getCalories() {
        return calories;
    }

    public float getTotalCalories() {
        if (data == null) {
            return calories;
        } else {
            float total = 0f;
            if (data.getType() == 0) {
                total += data.getCalories() * time * AppSettings.getInstance().getWeightDefault();
            } else {
                total += data.getCalories() * getTotalCount() * AppSettings.getInstance().getWeightDefault();
            }
            return total;
        }
    }

    public int getTotalCount() {
        if (data == null) {
            return 0;
        } else if (data.getType() == 1) {
            if (data.isTwoSides()) {
                return count * 2;
            } else {
                return count;
            }
        }
        return 0;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public Workout getData() {
        return data;
    }

    public void setData(Workout data) {
        this.data = data;
    }

    public String getTimeCountString() {
        if (data.getType() == 0) {
            return Utils.convertStringTime(time);
        } else {
            return "x" + (data.isTwoSides() ? count * 2 + "" : count + "");
        }
    }

    public String getTimeCountTitle(boolean forEachSide) {
        if (data.getType() == 0) {
            return time + "s";
        } else {
            if (forEachSide) {
                return "x " + count;
            } else {
                return "x " + (data.isTwoSides() ? count : count * 2);
            }
        }
    }

    public String getTimeString() {
        int mins = time / 60;
        int secs = time % 60;
        return String.format(Locale.US, "%02d:%02d", mins, secs);
    }

    public boolean isTraining() {
        return isTraining;
    }

    public void setTraining(boolean training) {
        isTraining = training;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workoutUserId);
        dest.writeString(this.id);
        dest.writeInt(this.time);
        dest.writeInt(this.count);
        dest.writeInt(this.status);
        dest.writeFloat(this.calories);
        dest.writeByte(this.isReplaced ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTraining ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.data, flags);
    }

    protected WorkoutUser(Parcel in) {
        this.workoutUserId = in.readString();
        this.id = in.readString();
        this.time = in.readInt();
        this.count = in.readInt();
        this.status = in.readInt();
        this.calories = in.readFloat();
        this.isReplaced = in.readByte() != 0;
        this.isTraining = in.readByte() != 0;
        this.data = in.readParcelable(Workout.class.getClassLoader());
    }

    public static final Creator<WorkoutUser> CREATOR = new Creator<WorkoutUser>() {
        @Override
        public WorkoutUser createFromParcel(Parcel source) {
            return new WorkoutUser(source);
        }

        @Override
        public WorkoutUser[] newArray(int size) {
            return new WorkoutUser[size];
        }
    };
}
