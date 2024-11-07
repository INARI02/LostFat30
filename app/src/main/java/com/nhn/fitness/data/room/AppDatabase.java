package com.nhn.fitness.data.room;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nhn.fitness.R;
import com.nhn.fitness.data.dao.ChallengeDayUserDao;
import com.nhn.fitness.data.dao.DailySectionUserDao;
import com.nhn.fitness.data.dao.DayHistoryDao;
import com.nhn.fitness.data.dao.ReminderDao;
import com.nhn.fitness.data.dao.SectionHistoryDao;
import com.nhn.fitness.data.dao.SectionUserDao;
import com.nhn.fitness.data.dao.StepDao;
import com.nhn.fitness.data.dao.WorkoutUserDao;
import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.dto.SectionDTO;
import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.dto.StepDTO;
import com.nhn.fitness.data.model.ChallengeDay;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.data.model.DailySection;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.model.Section;
import com.nhn.fitness.data.model.SectionHistory;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.model.Step;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.DailySectionRepository;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.data.repositories.SectionHistoryRepository;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.data.repositories.StepRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.ui.base.BaseApplication;
import com.nhn.fitness.ui.interfaces.DatabaseListener;
import com.nhn.fitness.utils.DataConverter;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Database(entities = {SectionUser.class, WorkoutUser.class, DayHistoryModel.class, Reminder.class,
        ChallengeDayUser.class, SectionHistory.class, DailySectionUser.class, Step.class},
        version = 4,
        exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "app_database.db";
    private static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    BaseApplication.getInstance().getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.e("status", "local db created");
//                            initAppDatabase();
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                        }
                    })
                    .build();
        }
        return instance;
    }

    public abstract SectionUserDao sectionUserDao();

    public abstract WorkoutUserDao workoutUserDao();

    public abstract DayHistoryDao dayHistoryDao();

    public abstract ReminderDao reminderDao();

    public abstract ChallengeDayUserDao challengeDayUserDao();

    public abstract SectionHistoryDao sectionHistoryDao();

    public abstract DailySectionUserDao dailySectionUserDao();

    public abstract StepDao stepDao();

    @SuppressLint("CheckResult")
    public static void initAppDatabase(DatabaseListener listener) {
        Log.e("status", "initDatabase");
        insertAllWorkoutUser(listener);
        initReminder();
    }

    /**
     * Xoa toan bo du lieu nguoi dung tren database
     */
    public void clearUserData() {
        DailySectionRepository.getInstance().resetAll();
        DayHistoryRepository.getInstance().deleteAll().subscribe();
        ReminderRepository.getInstance().deleteAll().subscribe();
        SectionHistoryRepository.getInstance().deleteAll().subscribe();
        StepRepository.getInstance().deleteAll().subscribe();
        SectionRepository.getInstance().deleteAllUserAdded();
    }

    public void fetchAllData(com.nhn.fitness.ui.interfaces.Callback onFetchDataDone) {
        final RestApiHelper restApiHelper = RestApiHelper.getInstance();
        final int userId = SessionManager.getInstance().getCurrentUser().getId();
        // set ngay sinh vao preferences
        AppSettings.getInstance().setBirthday(SessionManager.getInstance().getCurrentUserBirthdate().getTime());
        restApiHelper.getAllDayHistory(userId, new retrofit2.Callback<List<DayHistoryDTO>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(@NonNull Call<List<DayHistoryDTO>> call, @NonNull Response<List<DayHistoryDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DayHistoryModel lastDayHistory = new DayHistoryModel();
                        for (DayHistoryDTO dayHistoryDTO : response.body()) {
                            DayHistoryModel dayHistoryModel = DataConverter.toModel(dayHistoryDTO);
                            if (lastDayHistory.getId() < dayHistoryModel.getId()) {
                                lastDayHistory = dayHistoryModel;
                            }
                            DayHistoryRepository.getInstance().insertFetchedData(dayHistoryModel).subscribe();
                        }
                        AppSettings.getInstance().setHeightDefault(lastDayHistory.getHeight());
                        AppSettings.getInstance().setWeightDefault(lastDayHistory.getWeight());
                    }
                }
                restApiHelper.getDailySectionUser(userId, new retrofit2.Callback<List<DailySectionUserDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DailySectionUserDTO>> call, @NonNull Response<List<DailySectionUserDTO>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                for (DailySectionUserDTO dayHistoryDTO : response.body()) {
                                    DailySectionUser dayHistoryModel = DataConverter.toModel(dayHistoryDTO);
                                    DailySectionRepository.getInstance().updateFetchedData(dayHistoryModel).subscribe();
                                }
                            }
                        }
                        restApiHelper.getSectionHistories(userId, new retrofit2.Callback<List<SectionHistoryDTO>>() {
                            @Override
                            public void onResponse(Call<List<SectionHistoryDTO>> call, Response<List<SectionHistoryDTO>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        for (SectionHistoryDTO sectionHistoryDTO : response.body()) {
                                            SectionHistory sectionHistory = DataConverter.toModel(sectionHistoryDTO);
                                            SectionHistoryRepository.getInstance().insertFetchedData(sectionHistory).subscribe();
                                        }
                                    }
                                }
                                restApiHelper.getStep(userId, new retrofit2.Callback<List<StepDTO>>() {
                                    @Override
                                    public void onResponse(Call<List<StepDTO>> call, Response<List<StepDTO>> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {
                                                for (StepDTO stepDTO : response.body()) {
                                                    Step step = DataConverter.toModel(stepDTO);
                                                    StepRepository.getInstance().saveFetchStep(step);
                                                }
                                            }
                                        }
                                        restApiHelper.getSections(userId, new retrofit2.Callback<List<SectionDTO>>() {
                                            @Override
                                            public void onResponse(Call<List<SectionDTO>> call, Response<List<SectionDTO>> response) {
                                                if (response.isSuccessful()) {
                                                    if (response.body() != null) {
                                                        for (SectionDTO sectionDTO : response.body()) {
                                                            Section section = DataConverter.toModel(sectionDTO);
                                                            SectionRepository.getInstance().insert(section, true).subscribe(() -> {
                                                                SectionUser sectionUser = new SectionUser(section.getId());
                                                                sectionUser.setTraining(true);
                                                                sectionUser.setWorkoutsId(section.getWorkoutsId());
                                                                SectionRepository.getInstance().insert(sectionUser).subscribe();
                                                            });
                                                        }
                                                    }
                                                }
                                                onFetchDataDone.execute(null);
                                            }

                                            @Override
                                            public void onFailure(Call<List<SectionDTO>> call, Throwable t) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<List<StepDTO>> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<List<SectionHistoryDTO>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<DailySectionUserDTO>> call, @NonNull Throwable t) {
                        Log.d("DEBUG_REPO_TAG", "onFailure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<DayHistoryDTO>> call, @NonNull Throwable t) {
                Log.d("DEBUG_REPO_TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("CheckResult")
    private static void insertAllWorkoutUser(DatabaseListener listener) {
        AppDatabaseConst.getInstance().workoutDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single()).subscribe(response -> {
            Log.e("status", "getAllWorkout done: " + response.size());
            ArrayList<WorkoutUser> workoutUserList = new ArrayList<>();
            for (Workout workout : response) {
                WorkoutUser workoutUser = new WorkoutUser(workout.getId());
                workoutUser.setTime(workout.getTimeDefault());
                workoutUser.setCount(workout.getCountDefault());
                workoutUser.setCalories(workout.getCalories());
                workoutUserList.add(workoutUser);
            }
            AppDatabase.getInstance().workoutUserDao().insertAll(workoutUserList)
                    .subscribeOn(Schedulers.single())
                    .observeOn(Schedulers.single())
                    .subscribe(() -> {
                        Log.e("status", "insert all workoutUser done");
                        insertAllSectionUser(listener);
                    });
        });
    }

    private static void initReminder() {
        // Init reminder
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder(
                Utils.randomInt(),
                BaseApplication.getInstance().getResources().getString(R.string.default_reminder_warm_up),
                8,
                0,
                new boolean[]{true, true, true, true, true, true, true},
                false,
                true,
                true,
                Calendar.getInstance().getTimeInMillis()
        ));
        reminders.add(new Reminder(
                Utils.randomInt(),
                BaseApplication.getInstance().getResources().getString(R.string.default_reminder_sleepy),
                22,
                0,
                new boolean[]{true, true, true, true, true, true, true},
                false,
                true,
                true,
                Calendar.getInstance().getTimeInMillis() - 2000
        ));
        AppDatabase.getInstance().reminderDao().insertAll(reminders)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private static void insertAllSectionUser(DatabaseListener listener) {
        // Insert section
        AppDatabaseConst.getInstance().sectionDao().getAll()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(response -> {
                    ArrayList<SectionUser> sectionUsers = new ArrayList<>();
                    for (Section section : response) {
                        SectionUser sectionUser = new SectionUser(section.getId());
                        sectionUser.setStatus(section.getStatus());
                        sectionUser.setWorkoutsId(section.getWorkoutsId());
                        sectionUsers.add(sectionUser);
                    }

                    Log.e("status", "getAllSection done");

//            for (SectionUser sectionUser : sectionUsers) {
//                boolean exist = AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(sectionUser.getId()) != null;
//                if (exist) {
//                    AppDatabase.getInstance().sectionUserDao().update()
//                } else {
//
//                }
//            }

                    AppDatabase.getInstance().sectionUserDao().insertAll(sectionUsers)
                            .subscribeOn(Schedulers.single())
                            .observeOn(Schedulers.single())
                            .subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Log.e("status", "insertAll sectionUser done");
//                                    initChallenge();
                                    initDailySection(listener);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }

    @SuppressLint("CheckResult")
    private static void initDailySection(DatabaseListener listener) {
        Log.e("status", "init Daily section");
        AppDatabaseConst.getInstance().dailySectionDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dailySections -> {
                    Log.e("status", "dailysection: " + dailySections.size());
                    ArrayList<DailySectionUser> result = new ArrayList<>();
                    for (DailySection dailySection : dailySections) {
                        DailySectionUser dailySectionUser = new DailySectionUser();
                        dailySectionUser.setId(dailySection.getId());
                        dailySectionUser.setLevel(dailySection.getLevel());
                        dailySectionUser.setLocked(true);
                        dailySectionUser.setRestDay(dailySection.isRestDay());
                        dailySectionUser.setProgress(0f);
                        dailySectionUser.setCompleted(false);
                        dailySectionUser.setDay(dailySection.getDay());
                        result.add(dailySectionUser);
                    }
                    result.get(0).setLocked(false);
                    result.get(30).setLocked(false);
                    result.get(60).setLocked(false);
                    AppDatabase.getInstance().dailySectionUserDao().insertAll(result).subscribe(listener::insertAllCompleted, error -> {
                        listener.insertAllCompleted();
                    });
                });
    }

    @SuppressLint("CheckResult")
    private static void initChallenge() {
        AppDatabaseConst.getInstance().challengeDayDao().getAll()
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(challengeDays -> {

                    Log.e("status", "getAllChallenge done");

                    for (ChallengeDay challengeDay : challengeDays) {
                        // Insert Challenge to local DB
                        ChallengeDayUser challengeDayUser = new ChallengeDayUser(challengeDay.getId(), challengeDay.getId(), 0);
                        AppDatabase.getInstance().challengeDayUserDao().insertWithoutObserve(challengeDayUser);

                        // Insert new workout for section
                        generateNewWorkoutUserForSectionDailyUser(challengeDay.getSectionId());
                    }

                    Log.e("status", "insert all challengeUser done");
                });
    }

    private static void generateNewWorkoutUserForSectionDailyUser(String sectionId) {
        SectionUser sectionUser = AppDatabase.getInstance().sectionUserDao().findByIdWithoutObserve(sectionId);
        sectionUser.setData(AppDatabaseConst.getInstance().sectionDao().findByIdWithoutObserve(sectionId));
        ArrayList<WorkoutUser> workoutUserList = new ArrayList<>();
        ArrayList<String> newIds = new ArrayList<>();
        for (String workoutId : sectionUser.getData().getWorkoutsId()) {
            Workout workout = AppDatabaseConst.getInstance().workoutDao().findByIdWithoutObserve(workoutId);
            String newId = workoutId + "-" + Utils.randomString(5);
            newIds.add(newId);

            WorkoutUser workoutUser = new WorkoutUser(newId);
            workoutUser.setId(workoutId);
            workoutUser.setTime(workout.getTimeDefault());
            workoutUser.setCount(workout.getCountDefault());
            workoutUser.setCalories(workout.getCalories());
            workoutUserList.add(workoutUser);
        }
        sectionUser.setWorkoutsId(newIds);
        AppDatabase.getInstance().sectionUserDao().update(sectionUser).subscribe();

        AppDatabase.getInstance().workoutUserDao().insertAllWithReplace(workoutUserList)
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe();
    }

}

