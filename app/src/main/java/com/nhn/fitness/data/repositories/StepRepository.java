package com.nhn.fitness.data.repositories;

import com.nhn.fitness.data.dao.StepDao;
import com.nhn.fitness.data.dto.StepDTO;
import com.nhn.fitness.data.model.Step;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.utils.Utils;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepRepository {
    private static StepRepository instance;
    private final StepDao stepDao;

    private StepRepository() {
        stepDao = AppDatabase.getInstance().stepDao();
    }

    public static StepRepository getInstance() {
        if (instance == null) {
            instance = new StepRepository();
        }
        return instance;
    }

    public void insertNewDay(long date, int steps) {
        Step step = stepDao.getStepByDate(date);
        if (step == null && steps > 0) {
            // add to yesterday
            stepDao.addToLastEntry(steps);
            // add to day
            step = new Step(date, -steps);
            stepDao.save(step);
            StepDTO dto = step.toDTO();
            dto.setUserId(SessionManager.getInstance().getCurrentUser().getId());
            RestApiHelper.getInstance().saveStep(dto, new Callback<StepDTO>() {
                @Override
                public void onResponse(Call<StepDTO> call, Response<StepDTO> response) {

                }

                @Override
                public void onFailure(Call<StepDTO> call, Throwable t) {

                }
            });
        }
    }

    public int getSteps(long date) {
        Step step = stepDao.getStepByDate(date);
        if (step == null) {
            return 0;
        }
        return step.getSteps();
    }

    public int getCurrentSteps() {
        int re = getSteps(-1);
        return re == Integer.MIN_VALUE ? 0 : re;
    }

    public int getTotalWithoutToday() {
        return stepDao.getTotalWithoutToday(Utils.getToday());
    }

    public int getDaysWithoutToday() {
        int re =  stepDao.getValidDays(0, Utils.getToday());
        return Math.max(re, 0);
    }

    public int getDays() {
        // todays is not counted yet
        int re = this.getDaysWithoutToday() + 1;
        return re;
    }

    public void saveCurrentSteps(long date, int totalTodaySteps) {
        Step step = stepDao.getStepByDate(date);
        if (step != null) {
            step.setSteps(totalTodaySteps);
            stepDao.update(step);
        } else {
            step = new Step(date, totalTodaySteps);
            stepDao.save(step);
        }
        StepDTO dto = step.toDTO();
        dto.setUserId(SessionManager.getInstance().getCurrentUser().getId());
        RestApiHelper.getInstance().saveStep(dto, new Callback<StepDTO>() {
            @Override
            public void onResponse(Call<StepDTO> call, Response<StepDTO> response) {

            }

            @Override
            public void onFailure(Call<StepDTO> call, Throwable t) {

            }
        });
    }

    public Completable deleteAll() {
        return stepDao.deleteAll();
    }

    public void saveFetchStep(Step step) {
        Step old = stepDao.getByID(step.getUuid());
        if (old != null) {
            stepDao.update(step);
        } else {
            stepDao.save(step);
        }
    }
}
