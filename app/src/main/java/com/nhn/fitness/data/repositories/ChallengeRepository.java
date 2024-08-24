package com.nhn.fitness.data.repositories;

import com.nhn.fitness.data.model.ChallengeDay;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.room.AppDatabaseConst;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ChallengeRepository {

    private static ChallengeRepository instance;

    public static ChallengeRepository getInstance() {
        if (instance == null) {
            instance = new ChallengeRepository();
        }
        return instance;
    }

    public Flowable<List<ChallengeDayUser>> getAll() {
        return AppDatabase.getInstance().challengeDayUserDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable update(ChallengeDayUser challengeDayUser) {
        return AppDatabase.getInstance().challengeDayUserDao().update(challengeDayUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<ChallengeDayUser>> getChallengeDayUserWithFullDataByWeek(int week) {
        return Flowable.just(week)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Integer, Flowable<List<ChallengeDayUser>>>() {
                    @Override
                    public Flowable<List<ChallengeDayUser>> apply(Integer integer) throws Exception {
                        List<ChallengeDay> challengeDays = AppDatabaseConst.getInstance().challengeDayDao().findByWeekWithoutObserve(integer);
                        ArrayList<ChallengeDayUser> result = new ArrayList<>();
                        for (ChallengeDay challengeDay: challengeDays) {
                            result.add(getChallengeDayUserWithFullDataWithoutObserve(challengeDay.getId()));
                        }
                        return Flowable.just(result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<ChallengeDayUser> getChallengeDayUserWithFullData(String id) {
        return Flowable.just(id)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<String, Flowable<ChallengeDayUser>>) newId -> Flowable.just(getChallengeDayUserWithFullDataWithoutObserve(newId))).distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public ChallengeDayUser getChallengeDayUserWithFullDataWithoutObserve(String id) {
        ChallengeDayUser challengeDayUser = AppDatabase.getInstance().challengeDayUserDao().findByIdWithoutObserve(id);
        ChallengeDay challengeDay = AppDatabaseConst.getInstance().challengeDayDao().findByIdWithoutObserve(challengeDayUser.getChallengeId());
        challengeDayUser.setData(challengeDay);
        return challengeDayUser;
    }
}
