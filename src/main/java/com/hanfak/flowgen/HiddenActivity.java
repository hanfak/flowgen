package com.hanfak.flowgen;

import static java.lang.String.format;

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
        return format("-[hidden]->%n%s", activity);
    }
}
