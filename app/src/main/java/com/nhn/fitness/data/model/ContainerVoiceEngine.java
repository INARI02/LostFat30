package com.nhn.fitness.data.model;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Locale;

public class ContainerVoiceEngine {

    private String label;
    private String packageName;
    private ArrayList<String> voices;
    private Intent intent;

    public ContainerVoiceEngine() {

    }

    public ContainerVoiceEngine(final String label, final String packageName, final ArrayList<String> voices, final Intent intent) {

        this.label = label;
        this.packageName = packageName;
        this.voices = voices;
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(final Intent intent) {
        this.intent = intent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public ArrayList<String> getVoices() {
        return voices;
    }

    public void setVoices(final ArrayList<String> voices) {
        this.voices = voices;
    }

    public static String format(String voice) {
        String[] array = voice.split("-");
        StringBuilder builder = new StringBuilder();
        builder.append(array[0].substring(0, 3));
        builder.append("_");
        builder.append(array[1].substring(0, 2));
        return builder.toString();
    }

    public static Locale toLocal(String language) {
        String string = format(language);
        String[] array = string.split("_");
        return new Locale(array[0], array[1]);
    }
}
