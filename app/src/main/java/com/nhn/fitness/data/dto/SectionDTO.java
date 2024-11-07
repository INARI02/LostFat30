package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionDTO {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("thumbFemale")
    @Expose
    private String thumbFemale;
    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("workoutIds")
    @Expose
    private String workoutIds;
    @SerializedName("titleLanguage")
    @Expose
    private String titleLanguage;
    @SerializedName("descriptionLanguage")
    @Expose
    private String descriptionLanguage;
    @SerializedName("userId")
    @Expose
    private int userId;

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

    public String getWorkoutIds() {
        return workoutIds;
    }

    public void setWorkoutIds(String workoutIds) {
        this.workoutIds = workoutIds;
    }

    public String getTitleLanguage() {
        return titleLanguage;
    }

    public void setTitleLanguage(String titleLanguage) {
        this.titleLanguage = titleLanguage;
    }

    public String getDescriptionLanguage() {
        return descriptionLanguage;
    }

    public void setDescriptionLanguage(String descriptionLanguage) {
        this.descriptionLanguage = descriptionLanguage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
