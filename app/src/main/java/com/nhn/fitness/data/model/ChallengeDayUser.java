package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "challenge_user")
public class ChallengeDayUser implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String challengeId;
    private int state;
    @Ignore
    private ChallengeDay data;

    public ChallengeDayUser(String id, String challengeId, int state) {
        this.id = id;
        this.challengeId = challengeId;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ChallengeDay getData() {
        return data;
    }

    public void setData(ChallengeDay data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.challengeId);
        dest.writeInt(this.state);
        dest.writeParcelable(this.data, flags);
    }

    protected ChallengeDayUser(Parcel in) {
        this.id = in.readString();
        this.challengeId = in.readString();
        this.state = in.readInt();
        this.data = in.readParcelable(ChallengeDay.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChallengeDayUser> CREATOR = new Parcelable.Creator<ChallengeDayUser>() {
        @Override
        public ChallengeDayUser createFromParcel(Parcel source) {
            return new ChallengeDayUser(source);
        }

        @Override
        public ChallengeDayUser[] newArray(int size) {
            return new ChallengeDayUser[size];
        }
    };
}
