package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nhn.fitness.data.model.History;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM `history`")
    Flowable<List<History>> getAll();

    @Query("SELECT * FROM `history` WHERE id IN (:ids)")
    Flowable<List<History>> loadAllByIds(int[] ids);

    @Query("SELECT * FROM `history` WHERE id LIKE :id LIMIT 1")
    Flowable<History> findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(History... histories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(History history);

    @Delete
    Completable delete(History history);
}