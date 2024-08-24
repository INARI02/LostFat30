package com.nhn.fitness.ui.lib.timer;


public interface HourglassListener {


    /**
     * Method to be called every second by the {@link Hourglass}
     *
     * @param timeRemaining: Time remaining in milliseconds.
     */
    void onTimerTick(long timeRemaining);

    /**
     * Method to be called by {@link Hourglass} when the thread is getting  finished
     */
    void onTimerFinish();


}