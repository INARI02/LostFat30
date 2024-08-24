package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.DailySection;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface DailySectionDao {

    @Query("SELECT * FROM `daily_section`")
    Flowable<List<DailySection>> getAll();

    @Query("SELECT * FROM `daily_section` WHERE id IN (:ids)")
    Flowable<List<DailySection>> loadAllByIds(String[] ids);

    @Query("SELECT * FROM `daily_section` WHERE id LIKE :id LIMIT 1")
    Flowable<DailySection> findById(int id);

    @Query("SELECT * FROM `daily_section` WHERE id LIKE :id LIMIT 1")
    DailySection findByIdWithoutObserve(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<DailySection> dailySections);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DailySection dailySection);

    @Delete
    Completable delete(DailySection dailySection);

    @Update
    Completable update(DailySection dailySection);

}
