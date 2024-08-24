package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nhn.fitness.data.model.SectionHistory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface SectionHistoryDao {
    @Query("SELECT * FROM `section_history` ORDER BY id DESC")
    Flowable<List<SectionHistory>> getAll();

    @Query("SELECT * FROM `section_history` WHERE id >= :time ORDER BY id DESC")
    Flowable<List<SectionHistory>> getCurrentWeek(long time);

    @Query("SELECT * FROM `section_history` WHERE id IN (:ids)")
    Flowable<List<SectionHistory>> loadAllByIds(long[] ids);

    @Query("SELECT * FROM `section_history` WHERE id IN (:ids)")
    List<SectionHistory> loadAllByIdsWithoutObserve(long[] ids);

    @Query("SELECT * FROM `section_history` WHERE id LIKE :id LIMIT 1")
    Flowable<SectionHistory> findById(long id);

    @Query("SELECT * FROM `section_history` WHERE id LIKE :id LIMIT 1")
    SectionHistory findByIdWithoutObserve(long id);

    @Query("SELECT COUNT(id) FROM `section_history`")
    Flowable<Integer> getCount();

    @Query("SELECT SUM(calories) FROM `section_history`")
    Flowable<Float> sumCalories();

    @Query("SELECT SUM(totalTime) FROM `section_history`")
    Flowable<Integer> sumTotalTime();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(SectionHistory... sectionHistories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(SectionHistory sectionHistory);

    @Delete
    Completable delete(SectionHistory sectionHistory);

    @Query("DELETE FROM section_history")
    Completable deleteAll();
}
