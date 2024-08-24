package com.nhn.fitness.data.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhn.fitness.data.model.Workout;

import java.lang.reflect.Type;
import java.util.List;

public class WorkoutListConverters {
    @TypeConverter
    public String fromWorkoutList(List<Workout> workouts) {
        if (workouts == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Workout>>() {
        }.getType();
        return gson.toJson(workouts, type);
    }

    @TypeConverter
    public List<Workout> toWorkoutList(String workoutsString) {
        if (workoutsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Workout>>() {
        }.getType();
        return gson.fromJson(workoutsString, type);
    }
}
