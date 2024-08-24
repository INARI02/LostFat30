package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tips")
public class TipsArticle implements Parcelable {
    @PrimaryKey
    @NonNull
    private int id;
    private String title;
    private String image;
    private String content;

    public TipsArticle(int id, String title, String image, String content) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.content);
    }

    protected TipsArticle(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.image = in.readString();
        this.content = in.readString();
    }

    public static final Creator<TipsArticle> CREATOR = new Creator<TipsArticle>() {
        @Override
        public TipsArticle createFromParcel(Parcel source) {
            return new TipsArticle(source);
        }

        @Override
        public TipsArticle[] newArray(int size) {
            return new TipsArticle[size];
        }
    };
}
