package com.hanfak.flowgen;

import java.util.Objects;

import static java.lang.System.lineSeparator;

// TODO: Add a builder (Activities) implement Action ie Activities.activity("action2").thenDo("action2")
public class Activity implements Action {

    private static final String SIMPLE_DEFAULT_ACTIVITY_TEMPLATE = "%s%s%s%s";
    private final String name;
    private String swimLane;
    private String note;
    private String style = ";";

    private Activity(String name) {
        this.name = name;
    }

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

    public Activity send() {
        this.style = ">";
        return this;
    }

    public Activity receive() {
        this.style = "<";
        return this;
    }

    public Activity square() {
        this.style = "]";
        return this;
    }

    public Activity angled() {
        this.style = ">";
        return this;
    }

    public Activity with(Note note) {
        this.note = note.build();
        return this;
    }
    public Activity inSwimLane(String swimLane) {
        this.swimLane = swimLane;
        return this;
    }
    // TODO: Labels

    @Override
    public String build() {
        String activityPopulated = SIMPLE_DEFAULT_ACTIVITY_TEMPLATE.formatted(":", name, style, lineSeparator());
        if (Objects.nonNull(swimLane)) {
            if (Objects.nonNull(note)) {
                return "|%s|%n%s%n%s".formatted(swimLane, activityPopulated, note);
            }
            return "|%s|%n%s".formatted(swimLane, activityPopulated);
        }
        if (Objects.nonNull(note)) {
            return activityPopulated + lineSeparator() + note;
        }
        return activityPopulated;
    }
}
