package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public class Activity implements Action{

    private final String name;

    private Activity(String name) {
        this.name = name;
    }

    public static Activity activity(String name) {
        return new Activity(name);
    }

    @Override
    public String build() {
        return "%s%s%s%s".formatted(":", name, ";", lineSeparator());
    }
}
