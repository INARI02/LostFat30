package com.nhn.fitness.data.repositories;

import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.room.AppDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ReminderRepository {
    private static ReminderRepository instance;

    public static ReminderRepository getInstance() {
        if (instance == null) {
            instance = new ReminderRepository();
        }
        return instance;
    }

    public Completable delete(Reminder reminder) {
        return AppDatabase.getInstance().reminderDao().delete(reminder)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Completable update(Reminder reminder) {
        return AppDatabase.getInstance().reminderDao().update(reminder)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Completable insert(Reminder reminder) {
        return AppDatabase.getInstance().reminderDao().insert(reminder)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Flowable<List<Reminder>> getAll() {
        return AppDatabase.getInstance().reminderDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Boolean> hasReminder() {
        return AppDatabase.getInstance().reminderDao().countEnable()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Integer, Flowable<Boolean>>) integer -> Flowable.just(Boolean.valueOf(integer > 0 ? true : false)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAll() {
        return AppDatabase.getInstance().reminderDao().deleteAll();
    }
}
