package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.WorkoutUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WorkoutUserDao {

    @Query("SELECT * FROM `workout_user`")
    Flowable<List<WorkoutUser>> getAll();

    @Query("SELECT * FROM `workout_user` WHERE workoutUserId IN (:ids)")
    Flowable<List<WorkoutUser>> loadAllByIds(String[] ids);

    @Query("SELECT * FROM `workout_user` WHERE workoutUserId LIKE :id LIMIT 1")
    Flowable<WorkoutUser> findById(String id);

    @Query("SELECT * FROM `workout_user` WHERE workoutUserId LIKE :id LIMIT 1")
    WorkoutUser findByIdWithoutObserve(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<WorkoutUser> workoutUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllWithReplace(List<WorkoutUser> workoutUsers);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(WorkoutUser workoutUser);

    @Delete
    Completable delete(WorkoutUser workoutUser);

    @Update
    Completable update(WorkoutUser workoutUser);

    @Update
    void updateAll(List<WorkoutUser> workoutUsers);
}
