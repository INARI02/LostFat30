package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.DailySectionUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface DailySectionUserDao {

    @Query("SELECT * FROM `daily_section_user`")
    Single<List<DailySectionUser>> getAll();

    @Query("SELECT * FROM `daily_section_user` WHERE id IN (:ids)")
    Flowable<List<DailySectionUser>> loadAllByIds(List<String> ids);

    @Query("SELECT * FROM `daily_section_user` WHERE level LIKE :level")
    Flowable<List<DailySectionUser>> loadByLevel(int level);

    @Query("SELECT * FROM `daily_section_user` WHERE id LIKE :id LIMIT 1")
    Flowable<DailySectionUser> findById(int id);

    @Query("SELECT * FROM `daily_section_user` WHERE id LIKE :id LIMIT 1")
    DailySectionUser findByIdWithoutObserve(int id);

    @Query("SELECT * FROM `daily_section_user` WHERE day LIKE :day AND level LIKE :level LIMIT 1")
    DailySectionUser findByDayWithoutObserve(int day, int level);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<DailySectionUser> dailySectionUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(DailySectionUser dailySectionUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWithoutObserve(DailySectionUser dailySectionUser);

    @Delete
    Completable delete(DailySectionUser dailySectionUser);

    @Update
    Completable update(DailySectionUser dailySectionUser);

    @Update
    Completable update(List<DailySectionUser> dailySectionUsers);

    @Query("DELETE from daily_section_user")
    Completable deleteAll();
}
