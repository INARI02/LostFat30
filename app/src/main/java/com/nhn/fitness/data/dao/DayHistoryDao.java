package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.DayHistoryModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface DayHistoryDao {

    @Query("SELECT * FROM `day_history` ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAll();

    @Query("SELECT * FROM `day_history` WHERE id IN (:ids) ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> loadAllByIds(long[] ids);

    @Query("SELECT * FROM `day_history` WHERE weight > 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAllWeight();

    @Query("SELECT * FROM `day_history` WHERE waistline > 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAllWaistline();

    @Query("SELECT * FROM `day_history` WHERE calories > 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAllCalories();

    @Query("SELECT * FROM `day_history` WHERE exercises > 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAllExercises();

    @Query("SELECT * FROM `day_history` WHERE calories > 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> getAllWorkouts();

    @Query("SELECT * FROM `day_history` WHERE id LIKE :id LIMIT 1")
    Single<DayHistoryModel> findById(long id);

    @Query("SELECT * FROM `day_history` WHERE id LIKE :id LIMIT 1")
    DayHistoryModel findByIdWithoutObserve(long id);

    @Query("SELECT * FROM `day_history` WHERE weight > 0 ORDER BY id DESC LIMIT 1")
    Single<DayHistoryModel> findWeightNewest();

    @Query("SELECT * FROM `day_history` WHERE waistline > 0 ORDER BY id DESC LIMIT 1")
    Single<DayHistoryModel> findWaistlineNewest();

    @Query("SELECT * FROM `day_history` WHERE id >= :id ORDER BY id ASC")
    List<DayHistoryModel> findAfter(long id);

    @Query("SELECT * FROM `day_history` WHERE id >= :id AND weight > 0 ORDER BY id ASC")
    List<DayHistoryModel> findWeightAfter(long id);

    @Query("SELECT * FROM `day_history` WHERE id <= :id AND weight > 0 ORDER BY id DESC")
    List<DayHistoryModel> findWeightBefore(long id);

    @Query("SELECT * FROM `day_history` WHERE id >= :id AND waistline > 0 ORDER BY id ASC")
    List<DayHistoryModel> findWaistlineAfter(long id);

    @Query("SELECT * FROM `day_history` WHERE id <= :id AND waistline > 0 ORDER BY id DESC")
    List<DayHistoryModel> findWaistlineBefore(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(DayHistoryModel... histories);

    @Query("SELECT COUNT(id) FROM `day_history`")
    Single<Integer> getCount();

    @Query("SELECT MAX(weight) from `day_history` WHERE weight > 0")
    Flowable<Float> getMaxWeight();

    @Query("SELECT MIN(weight) from `day_history` WHERE weight > 0")
    Flowable<Float> getMinWeight();

    @Query("SELECT weight FROM `day_history` WHERE weight > 0 ORDER BY id DESC LIMIT 1")
    Flowable<Float> getCurrentWeight();

    @Query("SELECT MAX(waistline) from `day_history` WHERE waistline > 0")
    Flowable<Float> getMaxWaistline();

    @Query("SELECT MIN(waistline) from `day_history` WHERE waistline > 0")
    Flowable<Float> getMinWaistline();

    @Query("SELECT waistline FROM `day_history` WHERE waistline > 0 ORDER BY id DESC LIMIT 1")
    Flowable<Float> getCurrentWaistline();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DayHistoryModel history);

    @Update
    Completable update(DayHistoryModel history);

    @Delete
    Completable delete(DayHistoryModel history);

    @Query("SELECT * FROM `day_history` WHERE id >= :id AND weight >= 0 ORDER BY id ASC")
    Flowable<List<DayHistoryModel>> last30Day(long id);

    @Query("DELETE FROM `day_history`")
    Completable deleteAll();
}
