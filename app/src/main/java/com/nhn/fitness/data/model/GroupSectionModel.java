package com.nhn.fitness.data.model;

import java.util.List;

public class GroupSectionModel {

    private int type; // 0: normal, 1: favorite, 2: recent
    private String title;
    private List<String> sections;

    public GroupSectionModel(int type, String title, List<String> sections) {
        this.type = type;
        this.title = title;
        this.sections = sections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public int getType() {
        return type;
    }
}
