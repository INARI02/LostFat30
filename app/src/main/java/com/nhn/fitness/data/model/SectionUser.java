package com.nhn.fitness.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.nhn.fitness.R;
import com.nhn.fitness.data.room.StringListConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "section_user")
public class SectionUser implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private boolean isFavorite = false;
    private boolean isTraining = false;
    private int status = 0;    // 0: open, 1: locked,
    private String trainingName = "";
    private Date updated;
    @TypeConverters(StringListConverters.class)
    private List<String> workoutsId;
    @Ignore
    private Section data;

    @Ignore
    public SectionUser(@NonNull String id) {
        this.id = id;
        workoutsId = new ArrayList<>();
        updated = new Date();
    }

    public SectionUser(@NonNull String id, boolean isFavorite, boolean isTraining, int status, Date updated, List<String> workoutsId, String trainingName) {
        this.id = id;
        this.isFavorite = isFavorite;
        this.isTraining = isTraining;
        this.status = status;
        this.updated = updated;
        this.workoutsId = workoutsId;
        this.trainingName = trainingName;
    }

    public Section getData() {
        return data;
    }

    public void setData(Section data) {
        this.data = data;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public List<String> getWorkoutsId() {
        return workoutsId;
    }

    public void setWorkoutsId(List<String> workoutsId) {
        this.workoutsId.clear();
        this.workoutsId.addAll(workoutsId);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isTraining() {
        return isTraining;
    }

    public void setTraining(boolean training) {
        isTraining = training;
    }

    public void reverseFavorite() {
        this.isFavorite = !isFavorite;
        updated = new Date();
//        Log.e("status", "update " + updated.getTime() + " - " + getData().getTitle() + " - ");
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitleHistory(Context context) {
        if (data != null) {
            if (data.getType() == 0) {
                return data.getTitleDisplay();
            } else {
                return String.format(context.getResources().getString(R.string.title_history_fullbody), data.getTitleDisplay());
            }
        } else {
            return context.getResources().getString(R.string.title_history_unknown);
        }
    }

    public String getTitleHistory(Context context, int level) {
        if (data != null) {
            if (data.getType() == 0) {
                return data.getTitleDisplay();
            } else {
                if (level == 1) {
                    return String.format(context.getResources().getString(R.string.title_history_fullbody), data.getTitleDisplay());
                } else if (level == 2) {
                    return String.format(context.getResources().getString(R.string.title_history_fullbody_2), data.getTitleDisplay());
                } else {
                    return String.format(context.getResources().getString(R.string.title_history_fullbody_3), data.getTitleDisplay());
                }
            }
        } else {
            return context.getResources().getString(R.string.title_history_unknown);
        }
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTraining ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
        dest.writeString(this.trainingName);
        dest.writeLong(this.updated != null ? this.updated.getTime() : -1);
        dest.writeStringList(this.workoutsId);
        dest.writeParcelable(this.data, flags);
    }

    protected SectionUser(Parcel in) {
        this.id = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.isTraining = in.readByte() != 0;
        this.status = in.readInt();
        this.trainingName = in.readString();
        long tmpUpdated = in.readLong();
        this.updated = tmpUpdated == -1 ? null : new Date(tmpUpdated);
        this.workoutsId = in.createStringArrayList();
        this.data = in.readParcelable(Section.class.getClassLoader());
    }

    public static final Creator<SectionUser> CREATOR = new Creator<SectionUser>() {
        @Override
        public SectionUser createFromParcel(Parcel source) {
            return new SectionUser(source);
        }

        @Override
        public SectionUser[] newArray(int size) {
            return new SectionUser[size];
        }
    };
}
