package com.nhn.fitness.data.repositories;

import static com.nhn.fitness.data.shared.AppSettings.DEBUG_REPO_TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayHistoryRepository {
    private static DayHistoryRepository instance;
    private final RestApiHelper restApiHelper;
    private final SessionManager sessionManager;

    private DayHistoryRepository() {
        restApiHelper = RestApiHelper.getInstance();
        sessionManager = SessionManager.getInstance();
    }

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
        DayHistoryDTO dayHistoryDTO = dayHistoryModel.toDTO();
        dayHistoryDTO.setUserId(sessionManager.getCurrentUser().getId());
        restApiHelper.saveDayHistory(dayHistoryDTO, new Callback<DayHistoryDTO>() {
            @Override
            public void onResponse(@NonNull Call<DayHistoryDTO> call, @NonNull Response<DayHistoryDTO> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<DayHistoryDTO> call, @NonNull Throwable t) {
                Log.d(DEBUG_REPO_TAG, "onFailure: " + t.getMessage());
            }
        });
        return AppDatabase.getInstance().dayHistoryDao().update(dayHistoryModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(DayHistoryModel dayHistoryModel) {
        DayHistoryDTO dayHistoryDTO = dayHistoryModel.toDTO();
        dayHistoryDTO.setUserId(sessionManager.getCurrentUser().getId());
        restApiHelper.saveDayHistory(dayHistoryDTO, new Callback<DayHistoryDTO>() {
            @Override
            public void onResponse(@NonNull Call<DayHistoryDTO> call, @NonNull Response<DayHistoryDTO> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<DayHistoryDTO> call, @NonNull Throwable t) {
                Log.d(DEBUG_REPO_TAG, "onFailure: " + t.getMessage());
            }
        });
        return AppDatabase.getInstance().dayHistoryDao().insert(dayHistoryModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Ham chi goi khi fetch du lieu tu server ve
     * @param dayHistoryModel
     * @return
     */
    public Completable insertFetchedData(DayHistoryModel dayHistoryModel) {
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
