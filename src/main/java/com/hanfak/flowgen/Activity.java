package com.hanfak.flowgen;

import java.util.Objects;

import static java.lang.System.lineSeparator;

// TODO: Add a builder (Activities) implement Action ie Activities.activity("action2").thenDo("action2")
public class Activity implements Action {

    private static final String SIMPLE_DEFAULT_ACTIVITY_TEMPLATE = "%s%s%s%s";
    private final String name;
    private String note;

    private Activity(String name) {
        this.name = name;
    }
    // TODO: pass in Content factory (build to string). a builder that build a multiline content (using queue) and have methods for bold, tables, list, lines etc

    public static Activity doActivity(String name) {
        return new Activity(name);
    }

    public static Activity activity(String name) {
        return new Activity(name);
    }

    public static Activity andActivity(String name) {
        return new Activity(name);
    }

    public static Activity thenActivity(String name) {
        return new Activity(name);
    }

    public Activity with(Note note) {
        this.note = note.build();
        return this;
    }

    // TODO: Labels

    @Override
    public String build() {
        String activityPopulated = SIMPLE_DEFAULT_ACTIVITY_TEMPLATE.formatted(":", name, ";", lineSeparator());
        if (Objects.nonNull(note)) {
            return activityPopulated + "\n" + note;
        }
        return activityPopulated;
    }
}
