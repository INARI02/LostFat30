package com.nhn.fitness.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.nhn.fitness.data.room.StringListConverters;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.utils.Utils;

import java.util.List;

@Entity(tableName = "workout")
public class Workout implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private int timeDefault;
    private int countDefault;
    private String video;
    @ColumnInfo(name = "video_female")
    private String videoFemale;
    private String anim;
    @ColumnInfo(name = "anim_female")
    private String animFemale;
    private int type = 0;    // 0: byTime, 1: byCount
    private boolean isTwoSides = false;
    private float calories;    // sum of time or per step * count * isTwoSides
    @TypeConverters(StringListConverters.class)
    private List<String> group;
    @ColumnInfo(name = "title_language")
    private MultiLanguageModel titleLanguage;
    @ColumnInfo(name = "description_language")
    private MultiLanguageModel descriptionLanguage;
    @ColumnInfo(name = "is_training")
    private boolean isTraining;


    public Workout(@NonNull String id, String title, String description, int timeDefault, int countDefault, String video, String anim, String videoFemale, String animFemale, int type, boolean isTwoSides, float calories, List<String> group, MultiLanguageModel titleLanguage, MultiLanguageModel descriptionLanguage, boolean isTraining) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeDefault = timeDefault;
        this.countDefault = countDefault;
        this.video = video;
        this.videoFemale = videoFemale;
        this.anim = anim;
        this.animFemale = animFemale;
        this.type = type;
        this.isTwoSides = isTwoSides;
        this.calories = calories;
        this.group = group;
        this.titleLanguage = titleLanguage;
        this.descriptionLanguage = descriptionLanguage;
        this.isTraining = isTraining;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeDefault() {
        return timeDefault;
    }

    public void setTimeDefault(int timeDefault) {
        this.timeDefault = timeDefault;
    }

    public int getCountDefault() {
        return countDefault;
    }

    public void setCountDefault(int countDefault) {
        this.countDefault = countDefault;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAnim() {
        return anim;
    }

    public String getImageGender() {
        if (AppSettings.getInstance().getGender() == 0) {
            return getAnimFemale();
        } else {
            return getAnim();
        }
    }

    public void setAnim(String anim) {
        this.anim = anim;
    }

    public String getVideoGender() {
        if (AppSettings.getInstance().getGender() == 0) {
            return getVideoFemale();
        } else {
            return getVideo();
        }
    }

    public String getVideoFemale() {
        return videoFemale;
    }

    public void setVideoFemale(String videoFemale) {
        this.videoFemale = videoFemale;
    }

    public String getAnimFemale() {
        return animFemale;
    }

    public void setAnimFemale(String animFemale) {
        this.animFemale = animFemale;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isTwoSides() {
        return isTwoSides;
    }

    public void setTwoSides(boolean twoSides) {
        isTwoSides = twoSides;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public MultiLanguageModel getTitleLanguage() {
        return titleLanguage;
    }

    public void setTitleLanguage(MultiLanguageModel titleLanguage) {
        this.titleLanguage = titleLanguage;
    }

    public MultiLanguageModel getDescriptionLanguage() {
        return descriptionLanguage;
    }

    public void setDescriptionLanguage(MultiLanguageModel descriptionLanguage) {
        this.descriptionLanguage = descriptionLanguage;
    }

    public String getTitleDisplay() {
        String language = AppSettings.getInstance().getLanguage();
        if (language.equals("en")) {
            return title;
        }
        if (titleLanguage.getHashMap().containsKey(language)) {
            return titleLanguage.getHashMap().get(language);
        } else {
            return title;
        }
    }

    public String getDescriptionDisplay() {
        String language = AppSettings.getInstance().getLanguage();
        if (language.equals("en")) {
            return description;
        }
        if (descriptionLanguage.getHashMap().containsKey(language)) {
            return descriptionLanguage.getHashMap().get(language);
        } else {
            return description;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Workout) obj).id);
    }

    public String getTimeCountString() {
        if (getType() == 0) {
            return Utils.convertStringTime(timeDefault);
        } else {
            return "x" + (isTwoSides() ? countDefault * 2 + "" : countDefault + "");
        }
    }

    public String getTimeCountTitle(boolean forEachSide) {
        if (getType() == 0) {
            return timeDefault + "s";
        } else {
            if (forEachSide) {
                return "x " + countDefault;
            } else {
                return "x " + (isTwoSides() ? countDefault : countDefault * 2);
            }
        }
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
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.timeDefault);
        dest.writeInt(this.countDefault);
        dest.writeString(this.video);
        dest.writeString(this.videoFemale);
        dest.writeString(this.anim);
        dest.writeString(this.animFemale);
        dest.writeInt(this.type);
        dest.writeByte(this.isTwoSides ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.calories);
        dest.writeStringList(this.group);
        dest.writeParcelable(this.titleLanguage, flags);
        dest.writeParcelable(this.descriptionLanguage, flags);
        dest.writeByte(this.isTraining ? (byte) 1 : (byte) 0);
    }

    protected Workout(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.timeDefault = in.readInt();
        this.countDefault = in.readInt();
        this.video = in.readString();
        this.videoFemale = in.readString();
        this.anim = in.readString();
        this.animFemale = in.readString();
        this.type = in.readInt();
        this.isTwoSides = in.readByte() != 0;
        this.calories = in.readFloat();
        this.group = in.createStringArrayList();
        this.titleLanguage = in.readParcelable(MultiLanguageModel.class.getClassLoader());
        this.descriptionLanguage = in.readParcelable(MultiLanguageModel.class.getClassLoader());
        this.isTraining = in.readByte() != 0;
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel source) {
            return new Workout(source);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };
}
