package com.nhn.fitness.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.nhn.fitness.data.dto.StepDTO;

import java.util.UUID;

@Entity(tableName = "step")
public class Step {
    @PrimaryKey
    @NonNull
    private String uuid;
    private long date;
    private int steps;

    public Step() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Step(long date, int steps) {
        this.uuid = UUID.randomUUID().toString();
        this.date = date;
        this.steps = steps;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public StepDTO toDTO() {
        StepDTO dto = new StepDTO();
        dto.setUuid(this.uuid);
        dto.setDate(this.date);
        dto.setSteps(this.steps);
        return dto;
    }
}
