package com.nhn.fitness.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nhn.fitness.data.model.SectionUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SectionUserDao {

    @Query("SELECT * FROM `section_user`")
    Single<List<SectionUser>> getAll();

    @Query("SELECT * FROM `section_user` where isFavorite = 1")
    Flowable<List<SectionUser>> getAllFavorite();

    @Query("SELECT * FROM `section_user` where isTraining = 1")
    Flowable<List<SectionUser>> getAllTraining();

    @Query("SELECT * FROM `section_user` WHERE id IN (:ids)")
    Flowable<List<SectionUser>> loadAllByIds(String[] ids);

    @Query("SELECT * FROM `section_user` WHERE id LIKE :id LIMIT 1")
    Single<SectionUser> findById(String id);

    @Query("SELECT * FROM `section_user` WHERE id LIKE :id LIMIT 1")
    SectionUser findByIdWithoutObserve(String id);

    @Update
    Completable update(SectionUser sectionUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<SectionUser> sectionUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(SectionUser sectionUser);

    @Delete
    Completable delete(SectionUser sectionUser);

    @Update
    void updateAll(List<SectionUser> sectionUsers);

    @Query("DELETE FROM section_user WHERE isTraining = 1")
    Completable deleteAllTraining();
}