package com.hanfak.flowgen;

public class HiddenActivity implements Action {

    private final String activity;

    HiddenActivity(String activity) {
        this.activity = activity;
    }

    static HiddenActivity hiddenActivity(String activity) {
        return new HiddenActivity(activity);
    }

    @Override
    public String build() {
        return "%s%n%s".formatted("-[hidden]->", activity);
    }
}
