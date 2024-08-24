package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.ChallengeDay;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ChallengeDayDao {

    @Query("SELECT * FROM `challenge`")
    Flowable<List<ChallengeDay>> getAll();

    @Query("SELECT * FROM `challenge` WHERE id IN (:ids)")
    Flowable<List<ChallengeDay>> loadAllByIds(String[] ids);

    @Query("SELECT * FROM `challenge` WHERE id LIKE :id LIMIT 1")
    Flowable<ChallengeDay> findById(String id);

    @Query("SELECT * FROM `challenge` WHERE id LIKE :id LIMIT 1")
    ChallengeDay findByIdWithoutObserve(String id);

    @Query("SELECT * FROM `challenge` WHERE week = :week")
    List<ChallengeDay> findByWeekWithoutObserve(int week);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<ChallengeDay> challengeDays);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ChallengeDay challengeDay);

    @Delete
    Completable delete(ChallengeDay challengeDay);

    @Update
    Completable update(ChallengeDay challengeDay);
}
