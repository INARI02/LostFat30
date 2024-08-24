package com.nhn.fitness.data.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nhn.fitness.BuildConfig;
import com.nhn.fitness.data.dao.ChallengeDayDao;
import com.nhn.fitness.data.dao.DailySectionDao;
import com.nhn.fitness.data.dao.SectionDao;
import com.nhn.fitness.data.dao.TipsArticleDao;
import com.nhn.fitness.data.dao.WorkoutDao;
import com.nhn.fitness.data.model.ChallengeDay;
import com.nhn.fitness.data.model.DailySection;
import com.nhn.fitness.data.model.Section;
import com.nhn.fitness.data.model.TipsArticle;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.ui.base.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@Database(entities = {Section.class, Workout.class, ChallengeDay.class, DailySection.class, TipsArticle.class},
        version = BuildConfig.DB_VERSION,
        exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class AppDatabaseConst extends RoomDatabase {
    private static final String DATABASE_NAME = "WorkOut.db";
    private static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    private static AppDatabaseConst instance;

    public static void clearInstance() {
        Log.e("status", "clear instance const db");
        instance = null;
    }

    public static AppDatabaseConst getInstance() {
        if (instance == null) {
            copyAttachedDatabase(BaseApplication.getInstance(), false);
            instance = Room.databaseBuilder(
                    BaseApplication.getInstance().getApplicationContext(),
                    AppDatabaseConst.class, BuildConfig.DB_VERSION + "_" + DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.e("status", "const db created");
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

    public abstract SectionDao sectionDao();

    public abstract WorkoutDao workoutDao();

    public abstract ChallengeDayDao challengeDayDao();

    public abstract DailySectionDao dailySectionDao();

    public abstract TipsArticleDao tipsArticleDao();

    public static void copyAttachedDatabase(Context context, boolean deleteIfExist) {
        File dbPath = context.getDatabasePath(BuildConfig.DB_VERSION + "_" + AppDatabaseConst.DATABASE_NAME);

        // If the database already exists, return
        if (dbPath.exists()) {
            try {
                if (deleteIfExist) {
                    boolean success = dbPath.delete();
                    Log.e("status", "delete db success: " + success);
                    dbPath = context.getDatabasePath(AppDatabaseConst.DATABASE_NAME);
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Make sure we have a path to the file
        Objects.requireNonNull(dbPath.getParentFile()).mkdirs();

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + AppDatabaseConst.DATABASE_NAME);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("Database", "Failed to open file", e);
            e.printStackTrace();
        }

        Log.e("status", "finish copy .db");
    }
}
