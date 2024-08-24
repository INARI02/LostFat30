package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nhn.fitness.data.model.Workout;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WorkoutDao {

    @Query("SELECT * FROM `workout`")
    Flowable<List<Workout>> getAll();

    @Query("SELECT * FROM workout WHERE is_training = 1")
    Flowable<List<Workout>> getAllTraining();

    @Query("SELECT * FROM `workout` WHERE id IN (:ids)")
    Flowable<List<Workout>> loadAllByIds(List<String> ids);

    @Query("SELECT * FROM `workout` WHERE id IN (:ids)")
    List<Workout> loadAllByIdsWithoutObserve(String[] ids);

    @Query("SELECT * FROM `workout` WHERE id LIKE :id LIMIT 1")
    Flowable<Workout> findById(String id);

    @Query("SELECT * FROM `workout` WHERE `group` LIKE '%\"' || :group || '\"%'")
    Flowable<List<Workout>> findByGroup(String group);

    @Query("SELECT * FROM `workout` WHERE id LIKE :id LIMIT 1")
    Workout findByIdWithoutObserve(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Workout... workouts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Workout workout);

    @Delete
    Completable delete(Workout workout);

}