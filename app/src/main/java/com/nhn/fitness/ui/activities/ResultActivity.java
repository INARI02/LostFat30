package com.nhn.fitness.ui.activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.fragments.BMIFragment;

import java.util.Locale;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ResultActivity extends BaseActivity {

    private static final String TAG_NAME = ResultActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        playAudio();
        initAnimation();
        initViews();
        initEvents();
    }


    private void initEvents() {
        findViewById(R.id.btn_again).setOnClickListener(view -> {


        });
        findViewById(R.id.btn_share).setOnClickListener(view -> {

        });
        findViewById(R.id.btn_close).setOnClickListener(view -> {
            finish();
        });
    }

    private void playAudio() {
        if (AppSettings.getInstance().getSoundVoice() && AppSettings.getInstance().getSound()) {
            MediaPlayer.create(this, R.raw.congratulations).start();
        }
    }

    private void initAnimation() {
        KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);
        viewKonfetti.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewKonfetti.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewKonfetti.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 10f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(4000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(new Size(8, 5f))
                        .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                        .streamFor(150, 1500L);
            }
        });
    }

    private void initViews() {
        float totalCalories = getIntent().getFloatExtra("calories", 0f);
        int totalExercises = getIntent().getIntExtra("exercises", 0);
        int totalTime = getIntent().getIntExtra("timer", 0);
        int hours = totalTime / 60 / 60;
        int mins = (Math.round(totalTime / 60f)) % 60;
        ((TextView) findViewById(R.id.txt_exercises)).setText(totalExercises + "");
        ((TextView) findViewById(R.id.txt_calories)).setText(String.format(Locale.US, "%.0f", totalCalories));
        ((TextView) findViewById(R.id.txt_timer)).setText(String.format("%02d:%02d", hours, mins));
        addFragment(new BMIFragment(), R.id.bmi, null, false, -1);
    }

    @Override
    public void onBackPressed() {
        finish();
       // AdmobHelp.getInstance().showInterstitialAd(this, () -> finish());
    }
}
