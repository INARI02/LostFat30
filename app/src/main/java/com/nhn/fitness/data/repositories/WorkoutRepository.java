package com.nhn.fitness.data.repositories;

import android.annotation.SuppressLint;
import android.util.Log;

import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.room.AppDatabaseConst;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WorkoutRepository {
    private static WorkoutRepository instance;

    public static WorkoutRepository getInstance() {
        if (instance == null) {
            instance = new WorkoutRepository();
        }
        return instance;
    }

    public Completable insert(WorkoutUser workoutUser) {
        return AppDatabase.getInstance().workoutUserDao().insert(workoutUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(List<WorkoutUser> workoutUsers) {
        return AppDatabase.getInstance().workoutUserDao().insertAll(workoutUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<WorkoutUser>> getAllWorkoutUserByIdsWithFullData(List<String> list) {
        return Flowable.just("")
                .subscribeOn(Schedulers.io())
                .flatMap((Function<String, Flowable<List<WorkoutUser>>>) s -> {
                    ArrayList<WorkoutUser> workoutUsers = new ArrayList<>();
                    for (String id : list) {
                        Log.e("status", "get: " + id);
                        WorkoutUser workoutUser = AppDatabase.getInstance().workoutUserDao().findByIdWithoutObserve(id);
                        Log.e("status", (workoutUser == null) + "");
                        if (workoutUser != null) {
                            workoutUser.setData(AppDatabaseConst.getInstance().workoutDao().findByIdWithoutObserve(workoutUser.getId()));
                            workoutUsers.add(workoutUser);
                        }
                    }
                    return Flowable.just(workoutUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<WorkoutUser>> getAllTraining(List<String> list) {
        return Flowable.just("")
                .subscribeOn(Schedulers.io())
                .flatMap((Function<String, Flowable<List<WorkoutUser>>>) s -> {
                    ArrayList<WorkoutUser> workoutUsers = new ArrayList<>();
                    for (String id : list) {
                        Log.e("status", "get: " + id);
                        WorkoutUser workoutUser = AppDatabase.getInstance().workoutUserDao().findByIdWithoutObserve(id);
                        Log.e("status", (workoutUser == null) + "");
                        workoutUser.setData(AppDatabaseConst.getInstance().workoutDao().findByIdWithoutObserve(workoutUser.getId()));
                        workoutUsers.add(workoutUser);
                    }
                    return Flowable.just(workoutUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

//    public Flowable<List<Workout>> getAllWorkoutByGroup(int group) {
//        return AppDatabaseConst.getInstance().workoutDao().findByGroup(group)
//                .distinctUntilChanged()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//    }

    public Workout getWorkoutById(String id) {
        return AppDatabaseConst.getInstance().workoutDao().findByIdWithoutObserve(id);
    }

    public Flowable<List<Workout>> getAllWorkoutByIdsWithFullData(List<String> ids) {
        return AppDatabaseConst.getInstance().workoutDao().loadAllByIds(ids)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Completable updateWorkoutUser(WorkoutUser workoutUser) {
        return AppDatabase.getInstance().workoutUserDao().update(workoutUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertWorkoutUser(WorkoutUser workoutUser) {
        return AppDatabase.getInstance().workoutUserDao().insert(workoutUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void resetAll() {
        AppDatabase.getInstance().workoutUserDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    for (WorkoutUser workoutUser : response) {
                        Workout workout = AppDatabaseConst.getInstance().workoutDao().findByIdWithoutObserve(workoutUser.getId());
                        workoutUser.setTime(workout.getTimeDefault());
                        workoutUser.setCount(workout.getCountDefault());
                        workoutUser.setCalories(workout.getCalories());
                        workoutUser.setReplaced(false);
                    }
                    AppDatabase.getInstance().workoutUserDao().updateAll(response);
                });
    }
}
