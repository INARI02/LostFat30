package com.nhn.fitness.data.repositories;

import static com.nhn.fitness.data.shared.AppSettings.DEBUG_REPO_TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.model.SectionHistory;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionHistoryRepository {
    private static SectionHistoryRepository instance;

    public static SectionHistoryRepository getInstance() {
        if (instance == null) {
            instance = new SectionHistoryRepository();
        }
        return instance;
    }

    public Flowable<List<SectionHistory>> getAll() {
        return AppDatabase.getInstance().sectionHistoryDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SectionHistory>> getAllFithFullData() {
        return AppDatabase.getInstance().sectionHistoryDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<SectionHistory>, Flowable<List<SectionHistory>>>) sectionHistories -> {
                    ArrayList<SectionHistory> result = new ArrayList(sectionHistories);
                    for (SectionHistory sectionHistory : result) {
                        sectionHistory.setData(AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(sectionHistory.getSectionId()));
                    }
                    return Flowable.just(result);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<SectionHistory>> getCurrentWeek() {
        Calendar calendar = Utils.getCurrentWeek().get(0).getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return AppDatabase.getInstance().sectionHistoryDao().getCurrentWeek(calendar.getTime().getTime())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<SectionHistory>, Flowable<List<SectionHistory>>>) sectionHistories -> {
                    ArrayList<SectionHistory> result = new ArrayList(sectionHistories);
                    for (SectionHistory sectionHistory : result) {
                        sectionHistory.setData(AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(sectionHistory.getSectionId()));
                    }
                    return Flowable.just(result);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(SectionHistory sectionHistory) {
        SectionHistoryDTO sectionHistoryDTO = sectionHistory.toDTO();
        sectionHistoryDTO.setUserId(SessionManager.getInstance().getCurrentUser().getId());
        RestApiHelper.getInstance().saveSectionHistory(sectionHistoryDTO, new Callback<SectionHistoryDTO>() {
            @Override
            public void onResponse(@NonNull Call<SectionHistoryDTO> call, @NonNull Response<SectionHistoryDTO> response) {

            }

            @Override
            public void onFailure(@NonNull Call<SectionHistoryDTO> call, @NonNull Throwable t) {
                Log.d(DEBUG_REPO_TAG, "onFailure: " + t.getMessage());
            }
        });
        return AppDatabase.getInstance().sectionHistoryDao().insert(sectionHistory)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Completable insertFetchedData(SectionHistory sectionHistory) {
        return AppDatabase.getInstance().sectionHistoryDao().insert(sectionHistory)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Flowable<Integer> getCount() {
        return AppDatabase.getInstance().sectionHistoryDao().getCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Float> sumCalories() {
        return AppDatabase.getInstance().sectionHistoryDao().sumCalories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Integer> sumTotalTime() {
        return AppDatabase.getInstance().sectionHistoryDao().sumTotalTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAll() {
        return AppDatabase.getInstance().sectionHistoryDao().deleteAll();
    }
}
