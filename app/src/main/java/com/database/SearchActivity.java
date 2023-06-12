package com.database;

public class SearchActivity {
    private String name;
    private String activityType;

    public SearchActivity(String name, String activityType) {
        this.name = name;
        this.activityType = activityType;
    }

    public String getName() {
        return name;
    }

    public String getActivityType() {
        return activityType;
    }
}
