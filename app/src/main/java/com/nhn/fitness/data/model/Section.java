package com.nhn.fitness.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.nhn.fitness.R;
import com.nhn.fitness.data.room.StringListConverters;
import com.nhn.fitness.data.shared.AppSettings;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "section")
public class Section implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private String thumb;
    @ColumnInfo(name = "thumb_female")
    private String thumbFemale;
    private int level = 0;
    private int type = 0;  // 0: Normal, 1: Daily
    private int status = 0;    // 0: open, 1: locked,
    @TypeConverters(StringListConverters.class)
    private List<String> workoutsId = new ArrayList<>();
    @ColumnInfo(name = "title_language")
    private MultiLanguageModel titleLanguage = new MultiLanguageModel();
    @ColumnInfo(name = "description_language")
    private MultiLanguageModel descriptionLanguage = new MultiLanguageModel();

    @Ignore
    public Section() {
    }

    public Section(@NonNull String id, String title, String description, String thumb, String thumbFemale, int level, int type, int status, List<String> workoutsId, MultiLanguageModel titleLanguage, MultiLanguageModel descriptionLanguage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumb = thumb;
        this.thumbFemale = thumbFemale;
        this.level = level;
        this.type = type;
        this.status = status;
        this.workoutsId = workoutsId;
        this.titleLanguage = titleLanguage;
        this.descriptionLanguage = descriptionLanguage;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbFemale() {
        return thumbFemale;
    }

    public void setThumbFemale(String thumbFemale) {
        this.thumbFemale = thumbFemale;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelString(Context context) {
        if (level == 1) {
            return context.getResources().getString(R.string.level_beginner).toUpperCase();
        } else if (level == 2) {
            return context.getResources().getString(R.string.level_intermediate).toUpperCase();
        } else if (level == 3) {
            return context.getResources().getString(R.string.level_advanced).toUpperCase();
        } else {
            return "";
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getWorkoutsId() {
        return workoutsId;
    }

    public String getNumberWorkoutsString(Context context) {
        return String.format(context.getResources().getString(R.string.number_workouts), workoutsId.size());
    }

    public void setWorkoutsId(List<String> workoutsId) {
        this.workoutsId = workoutsId;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.thumb);
        dest.writeString(this.thumbFemale);
        dest.writeInt(this.level);
        dest.writeInt(this.type);
        dest.writeInt(this.status);
        dest.writeStringList(this.workoutsId);
        dest.writeParcelable(this.titleLanguage, flags);
        dest.writeParcelable(this.descriptionLanguage, flags);
    }

    protected Section(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.thumb = in.readString();
        this.thumbFemale = in.readString();
        this.level = in.readInt();
        this.type = in.readInt();
        this.status = in.readInt();
        this.workoutsId = in.createStringArrayList();
        this.titleLanguage = in.readParcelable(MultiLanguageModel.class.getClassLoader());
        this.descriptionLanguage = in.readParcelable(MultiLanguageModel.class.getClassLoader());
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel source) {
            return new Section(source);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
}


