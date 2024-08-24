package com.nhn.fitness.data.repositories;

import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DayHistoryRepository {
    private static DayHistoryRepository instance;

    public static DayHistoryRepository getInstance() {
        if (instance == null) {
            instance = new DayHistoryRepository();
        }
        return instance;
    }

    public Flowable<List<DayHistoryModel>> getAll() {
        return AppDatabase.getInstance().dayHistoryDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DayHistoryModel>> getAllWorkouts() {
        return AppDatabase.getInstance().dayHistoryDao().getAllWorkouts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DayHistoryModel>> getAllWeight() {
        return AppDatabase.getInstance().dayHistoryDao().getAllWeight()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DayHistoryModel>> getAllWaistline() {
        return AppDatabase.getInstance().dayHistoryDao().getAllWaistline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DayHistoryModel>> getAllCalories() {
        return AppDatabase.getInstance().dayHistoryDao().getAllCalories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<DayHistoryModel>> getAllExercises() {
        return AppDatabase.getInstance().dayHistoryDao().getAllExercises()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<DayHistoryModel> getById(long id) {
        return AppDatabase.getInstance().dayHistoryDao().findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<DayHistoryModel>> getCurrentWeek() {
        DayHistoryModel firstDay = Utils.getCurrentWeek().get(0);
        return Single.just(firstDay.getId())
                .flatMap((Function<Long, Single<List<DayHistoryModel>>>) aLong -> {
                    ArrayList<DayHistoryModel> data = new ArrayList<>(AppDatabase.getInstance().dayHistoryDao().findAfter(aLong));
                    ArrayList<DayHistoryModel> result = new ArrayList<>(Utils.getCurrentWeek());
                    for (DayHistoryModel dayHistoryModel : result) {
                        for (DayHistoryModel history : data) {
                            if (dayHistoryModel.getId() == history.getId()) {
                                dayHistoryModel.setCalories(history.getCalories());
                                break;
                            }
                        }
                    }
                    return Single.just(result);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DayHistoryModel getByIdWithoutObserve(long id) {
        return AppDatabase.getInstance().dayHistoryDao().findByIdWithoutObserve(id);
    }

    public Single<DayHistoryModel> getWeightNewest() {
        return AppDatabase.getInstance().dayHistoryDao().findWeightNewest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<DayHistoryModel> getWaistlineNewest() {
        return AppDatabase.getInstance().dayHistoryDao().findWaistlineNewest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable update(DayHistoryModel dayHistoryModel) {
        return AppDatabase.getInstance().dayHistoryDao().update(dayHistoryModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(DayHistoryModel dayHistoryModel) {
        return AppDatabase.getInstance().dayHistoryDao().insert(dayHistoryModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Flowable<List<DayHistoryModel>> last30Days() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        return AppDatabase.getInstance().dayHistoryDao().last30Day(DateUtils.getIdDay(calendar))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAll() {
        return AppDatabase.getInstance().dayHistoryDao().deleteAll();
    }
}
