package com.nhn.fitness.data.repositories;

import android.annotation.SuppressLint;

import com.nhn.fitness.data.model.Section;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.room.AppDatabaseConst;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SectionRepository {
    private static SectionRepository instance;

    public static SectionRepository getInstance() {
        if (instance == null) {
            instance = new SectionRepository();
        }
        return instance;
    }

    public Completable delete(SectionUser sectionUser) {
        return AppDatabase.getInstance().sectionUserDao().delete(sectionUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Section section) {
        return AppDatabaseConst.getInstance().sectionDao().delete(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(Section section) {
        return AppDatabaseConst.getInstance().sectionDao().insert(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(SectionUser sectionUser) {
        return AppDatabase.getInstance().sectionUserDao().insert(sectionUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(List<SectionUser> sectionUsers) {
        return AppDatabase.getInstance().sectionUserDao().insertAll(sectionUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SectionUser>> getAllTrainingWithFullData() {
        return AppDatabase.getInstance().sectionUserDao().getAllTraining()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<SectionUser>, Flowable<List<SectionUser>>>) sectionUsers -> {
                    List<String> ids = new ArrayList<>();
                    for (SectionUser sectionUser : sectionUsers) {
                        // new insert if update
                        if (AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(sectionUser.getId()) == null) {
                            Section section = new Section();
                            section.setId(Utils.randomString(5));
                            section.setTitle(sectionUser.getTrainingName());
                            section.setDescription("My Training");
                            section.setThumb("absworkout1");
                            section.setThumbFemale("absworkout1");
                            section.setWorkoutsId(sectionUser.getWorkoutsId());
                            AppDatabaseConst.getInstance().sectionDao().insert(section).subscribe();
                            sectionUser.setData(section);
                        } else {
                            ids.add(sectionUser.getId());
                        }
                    }
                    List<Section> sections = AppDatabaseConst.getInstance().sectionDao().loadAllByIdsWithoutObserve(ids.toArray(new String[0]));
                    for (SectionUser mSectionUser : sectionUsers) {
                        for (Section mSection : sections) {
                            if (mSection.getId().equals(mSectionUser.getId())) {
                                mSectionUser.setData(mSection);
                            }
                        }
                    }
                    return Flowable.just(sectionUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<SectionUser>> getAllSectionUserWithFullData() {
        return AppDatabase.getInstance().sectionUserDao().getAll()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<SectionUser>, Single<List<SectionUser>>>) sectionUsers -> {
                    List<String> ids = new ArrayList<>();
                    for (SectionUser sectionUser : sectionUsers) {
                        ids.add(sectionUser.getId());
                    }
                    List<Section> sections = AppDatabaseConst.getInstance().sectionDao().loadAllByIdsWithoutObserve(ids.toArray(new String[0]));
                    for (int i = 0; i < sectionUsers.size(); i++) {
                        sectionUsers.get(i).setData(sections.get(i));
                    }
                    return Single.just(sectionUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<SectionUser> getSectionUserByIdWithFullData(String id) {
        return AppDatabase.getInstance().sectionUserDao().findById(id)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<SectionUser, Single<SectionUser>>) sectionUser -> {
                    Section section = AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(sectionUser.getId());
                    sectionUser.setData(section);
                    return Single.just(sectionUser);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SectionUser>> getAllSectionUserByIdsWithFullData(List<String> list) {
        return Flowable.just("")
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged()
                .flatMap((Function<String, Flowable<List<SectionUser>>>) s -> {
                    ArrayList<SectionUser> sectionUsers = new ArrayList<>();
                    for (String id : list) {
                        SectionUser sectionUser = AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(id);
                        sectionUser.setData(AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(id));
                        sectionUsers.add(sectionUser);
                    }
                    return Flowable.just(sectionUsers);
                })
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Section>> getSectionByIds(List<String> ids) {
        return AppDatabaseConst.getInstance().sectionDao()
                .loadAllByIds(ids.toArray(new String[0]))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SectionUser>> getAllSectionUserFavoriteWithFullData() {
        return AppDatabase.getInstance().sectionUserDao().getAllFavorite()
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged()
                .flatMap((Function<List<SectionUser>, Flowable<List<SectionUser>>>) sectionUsers -> {
                    List<String> ids = new ArrayList<>();
                    for (SectionUser sectionUser : sectionUsers) {
                        ids.add(sectionUser.getId());
                    }
                    List<Section> sections = AppDatabaseConst.getInstance().sectionDao().loadAllByIdsWithoutObserve(ids.toArray(new String[0]));
                    for (int i = 0; i < sectionUsers.size(); i++) {
                        sectionUsers.get(i).setData(sections.get(i));
                    }
                    return Flowable.just(sectionUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateSectionUser(SectionUser sectionUser) {
        return AppDatabase.getInstance().sectionUserDao().update(sectionUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateSection(Section section) {
        return AppDatabaseConst.getInstance().sectionDao().update(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void resetAll() {
        AppDatabase.getInstance().sectionUserDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    for (SectionUser sectionUser : response) {
                        Section section = AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(sectionUser.getId());
                        sectionUser.setFavorite(false);
                        sectionUser.setWorkoutsId(section.getWorkoutsId());
                    }
                    AppDatabase.getInstance().sectionUserDao().updateAll(response);
                });
    }

    public void deleteAllTraining() {
        AppDatabase.getInstance().sectionUserDao().deleteAllTraining()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }
}
