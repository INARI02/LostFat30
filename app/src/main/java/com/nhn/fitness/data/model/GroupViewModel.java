package com.nhn.fitness.data.model;

public class GroupViewModel {
    public static final int TYPE_GROUP_SECTION = 0;
    public static final int TYPE_FRAGMENT = 1;
    public static final int TYPE_LAYOUT = 2;
    public static final int TYPE_OTHER = 3;

    private final int type;
    private final Object data;

    public GroupViewModel(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
