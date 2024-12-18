package com.nhn.fitness.data.repositories;

import static com.nhn.fitness.data.shared.AppSettings.DEBUG_REPO_TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.model.DailySection;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.room.AppDatabaseConst;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailySectionRepository {

    private static DailySectionRepository instance;

    public static DailySectionRepository getInstance() {
        if (instance == null) {
            instance = new DailySectionRepository();
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void resetAll() {
        AppDatabase.getInstance().dailySectionUserDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    for (DailySectionUser dailySectionUser : response) {
                        dailySectionUser.setLocked(true);
                        dailySectionUser.setCompleted(false);
                        dailySectionUser.setProgress(0f);
                    }
                    response.get(0).setLocked(false);
                    response.get(30).setLocked(false);
                    response.get(60).setLocked(false);
                    AppDatabase.getInstance().dailySectionUserDao().update(response).subscribe();
                });
    }

    public Completable update(DailySectionUser dailySectionUser) {
        DailySectionUserDTO dto = dailySectionUser.toDTO();
        dto.setUserId(SessionManager.getInstance().getCurrentUser().getId());
        RestApiHelper.getInstance().saveDailySectionUser(dto, new Callback<DailySectionUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<DailySectionUserDTO> call, @NonNull Response<DailySectionUserDTO> response) {

            }

            @Override
            public void onFailure(@NonNull Call<DailySectionUserDTO> call, @NonNull Throwable t) {
                Log.d(DEBUG_REPO_TAG, "onFailure: " + t.getMessage());
            }
        });
        return AppDatabase.getInstance().dailySectionUserDao().update(dailySectionUser)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    /**
     * Chi dung khi fetch data tu server ve
     *
     * @param dailySectionUser
     * @return
     */
    public Completable updateFetchedData(DailySectionUser dailySectionUser) {
        return AppDatabase.getInstance().dailySectionUserDao().update(dailySectionUser)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Flowable<List<DailySectionUser>> getListByLevel(int level) {
        return AppDatabase.getInstance().dailySectionUserDao().loadByLevel(level)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<DailySectionUser>, Flowable<List<DailySectionUser>>>) dailySectionUsers -> {
                    for (DailySectionUser dailySectionUser : dailySectionUsers) {
                        dailySectionUser.setData(getDailySectionWithFullData(dailySectionUser.getId()));
                    }
                    return Flowable.just(dailySectionUsers == null ? new ArrayList<DailySectionUser>() : dailySectionUsers);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    DailySection getDailySectionWithFullData(int id) {
        DailySection dailySection = AppDatabaseConst.getInstance().dailySectionDao().findByIdWithoutObserve(id);
        SectionUser sectionUser = AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(dailySection.getSectionId());
        sectionUser.setData(AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(sectionUser.getId()));
        dailySection.setData(sectionUser);
        return dailySection;
    }

    @SuppressLint("CheckResult")
    public void nextDaily(DailySectionUser dailySectionUser) {
        Single.just(dailySectionUser)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (dailySectionUser.getDay() < 30) {
                        DailySectionUser next = AppDatabase.getInstance().dailySectionUserDao().findByDayWithoutObserve(dailySectionUser.getDay() + 1, dailySectionUser.getLevel());
                        if (next != null) {
                            next.setLocked(false);
                            update(next).subscribe();
                        }
                    }
                });
    }

}
