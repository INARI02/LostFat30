package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.Reminder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ReminderDao {

    @Query("SELECT * FROM `reminder` WHERE isDisplay = 1 ORDER BY timestamp DESC")
    Flowable<List<Reminder>> getAll();

    @Query("SELECT * FROM `reminder` WHERE id IN (:ids)")
    Flowable<List<Reminder>> loadAllByIds(int[] ids);

    @Query("SELECT * FROM `reminder` WHERE id LIKE :id LIMIT 1")
    Flowable<Reminder> findById(int id);

    @Query("SELECT COUNT(id) FROM `reminder` WHERE isEnable = 1 AND id != 1 AND id != 2")
    Flowable<Integer> countEnable();

    @Query("SELECT * FROM `reminder` WHERE id LIKE :id LIMIT 1")
    Reminder findByIdWithoutObserver(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Reminder... reminders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(ArrayList<Reminder> reminders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Reminder reminder);

    @Delete
    Completable delete(Reminder reminder);

    @Update
    Completable update(Reminder reminder);

    @Query("DELETE FROM reminder WHERE isAdmin = 0")
    Completable deleteAll();
}
