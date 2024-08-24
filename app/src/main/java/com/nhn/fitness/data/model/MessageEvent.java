package com.nhn.fitness.data.model;

public class MessageEvent {
    public static final String OPEN_WORKOUT_EVENT = "open_workout";
    public static final String STOP_AUDIO = "stop_audio";
    public static final String CHANGE_GENDER = "change_gender";

    private String key;
    private Object data;

    public MessageEvent(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
