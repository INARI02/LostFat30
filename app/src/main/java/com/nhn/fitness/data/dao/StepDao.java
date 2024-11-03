package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.Step;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface StepDao {

    @Insert
    void save(Step step);

    @Update
    void update(Step step);

    @Query("SELECT * FROM `step` WHERE uuid = :uuid")
    Step getByID(String uuid);

    @Query("SELECT * FROM `step`")
    List<Step> getAll();

    @Query("SELECT * FROM `step` ORDER BY date DESC LIMIT :limit")
    List<Step> getLastEntries(int limit);

    @Query("SELECT * FROM `step` WHERE date = :date")
    Step getStepByDate(long date);

    @Query("UPDATE `step` SET steps = :steps WHERE date = -1")
    void saveCurrentSteps(int steps);

    @Query("SELECT SUM(steps) FROM step WHERE steps > 0 AND date > 0 AND date < :today")
    int getTotalWithoutToday(long today);

    @Query("UPDATE step SET steps = steps + :steps WHERE date = (SELECT MAX(date) FROM step)")
    void addToLastEntry(int steps);

    @Query("SELECT COUNT(*) FROM step WHERE steps > :minSteps AND date < :today AND date > 0")
    int getValidDays(int minSteps, long today);

    @Query("DELETE FROM step")
    Completable deleteAll();
}
