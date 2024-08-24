package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.ChallengeDayUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ChallengeDayUserDao {

    @Query("SELECT * FROM `challenge_user`")
    Flowable<List<ChallengeDayUser>> getAll();

    @Query("SELECT * FROM `challenge_user` WHERE id IN (:ids)")
    Flowable<List<ChallengeDayUser>> loadAllByIds(List<String> ids);

    @Query("SELECT * FROM `challenge_user` WHERE id LIKE :id LIMIT 1")
    Flowable<ChallengeDayUser> findById(String id);

    @Query("SELECT * FROM `challenge_user` WHERE id LIKE :id LIMIT 1")
    ChallengeDayUser findByIdWithoutObserve(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<ChallengeDayUser> challengeDayUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ChallengeDayUser challengeDayUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWithoutObserve(ChallengeDayUser challengeDayUser);

    @Delete
    Completable delete(ChallengeDayUser challengeDayUser);

    @Update
    Completable update(ChallengeDayUser challengeDayUser);
}
