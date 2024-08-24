package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.Section;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SectionDao {

    @Query("SELECT * FROM `section`")
    Single<List<Section>> getAll();

    @Query("SELECT * FROM `section` WHERE type = 1")
    Flowable<List<Section>> getAllDaily();

    @Query("SELECT * FROM `section` WHERE id IN (:ids)")
    Flowable<List<Section>> loadAllByIds(String[] ids);

    @Query("SELECT * FROM `section` WHERE id IN (:ids)")
    List<Section> loadAllByIdsWithoutObserve(String[] ids);

    @Query("SELECT * FROM `section` WHERE id LIKE :id LIMIT 1")
    Flowable<Section> findById(String id);

    @Query("SELECT * FROM `section` WHERE id LIKE :id LIMIT 1")
    Section findByIdWithoutObserve(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Section... sections);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Section section);

    @Delete
    Completable delete(Section section);

    @Update
    Completable update(Section section);
}