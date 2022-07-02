package com.hanfak.flowgen;

import java.util.Objects;

import static java.lang.System.lineSeparator;

public class Activity implements Action {

    private static final String SIMPLE_DEFAULT_ACTIVITY_TEMPLATE = "%s%s%s%s";

    private final String name;

    private String swimLane;
    private String note;
    private String style = ";";
    private String arrowLabel;

    private Activity(String name) {
        this.name = name;
    }

    public static Activity activity(String name) {
        return new Activity(name);
    }

    public static Activity doActivity(String name) {
        return new Activity(name);
    }

    public static Activity andActivity(String name) {
        return new Activity(name);
    }

    public static Activity and(String name) {
        return new Activity(name);
    }

    public static Activity thenActivity(String name) {
        return new Activity(name);
    }

    public static Activity then(String name) {
        return new Activity(name);
    }

    public static Activity withActivity(String name) {
        return new Activity(name);
    }

    public static Activity with(String name) {
        return new Activity(name);
    }

    public static Activity executeActivity(String name) {
        return new Activity(name);
    }

    public static Activity execute(String name) {
        return new Activity(name);
    }

    public static Activity performActivity(String name) {
        return new Activity(name);
    }

    public static Activity perform(String name) {
        return new Activity(name);
    }

    public static Activity completeActivity(String name) {
        return new Activity(name);
    }

    public static Activity complete(String name) {
        return new Activity(name);
    }

    public static Activity finishActivity(String name) {
        return new Activity(name);
    }

    public static Activity finish(String name) {
        return new Activity(name);
    }

    public static Activity implementActivity(String name) {
        return new Activity(name);
    }

    public static Activity implement(String name) {
        return new Activity(name);
    }

    public static Activity prepareActivity(String name) {
        return new Activity(name);
    }

    public static Activity prepare(String name) {
        return new Activity(name);
    }

    public static Activity makeActivity(String name) {
        return new Activity(name);
    }

    public static Activity make(String name) {
        return new Activity(name);
    }

    public static Action fixActivity(String name) {
        return new Activity(name);
    }

    public static Action fix(String name) {
        return new Activity(name);
    }

    public static Action produceActivity(String name) {
        return new Activity(name);
    }

    public static Action produce(String name) {
        return new Activity(name);
    }

    public static Action arrangeActivity(String name) {
        return new Activity(name);
    }

    public static Action arrange(String name) {
        return new Activity(name);
    }

    public static Action createActivity(String name) {
        return new Activity(name);
    }

    public static Action create(String name) {
        return new Activity(name);
    }

    public static Action designActivity(String name) {
        return new Activity(name);
    }

    public static Action design(String name) {
        return new Activity(name);
    }

    public static Action workOutActivity(String name) {
        return new Activity(name);
    }

    public static Action workOut(String name) {
        return new Activity(name);
    }

    public static Action calculateActivity(String name) {
        return new Activity(name);
    }

    public static Action calculate(String name) {
        return new Activity(name);
    }

    public static Action solveActivity(String name) {
        return new Activity(name);
    }

    public static Action solve(String name) {
        return new Activity(name);
    }

    public static Action resolveActivity(String name) {
        return new Activity(name);
    }

    public static Action resolve(String name) {
        return new Activity(name);
    }

    public static Action sendActivity(String name) {
        return new Activity(name);
    }

    public static Action send(String name) {
        return new Activity(name);
    }

    public static Action listenActivity(String name) {
        return new Activity(name);
    }

    public static Action listen(String name) {
        return new Activity(name);
    }

    public static Action listenForActivity(String name) {
        return new Activity(name);
    }

    public static Action listenFor(String name) {
        return new Activity(name);
    }

    public static Action writeActivity(String name) {
        return new Activity(name);
    }

    public static Action write(String name) {
        return new Activity(name);
    }

    public static Action updateActivity(String name) {
        return new Activity(name);
    }

    public static Action update(String name) {
        return new Activity(name);
    }

    public static Action deleteActivity(String name) {
        return new Activity(name);
    }

    public static Action delete(String name) {
        return new Activity(name);
    }

    public static Action readActivity(String name) {
        return new Activity(name);
    }

    public static Action read(String name) {
        return new Activity(name);
    }

    public static Action fetchActivity(String name) {
        return new Activity(name);
    }

    public static Action fetch(String name) {
        return new Activity(name);
    }

    public static Action retrieveActivity(String name) {
        return new Activity(name);
    }

    public static Action retrieve(String name) {
        return new Activity(name);
    }

    public static Action useActivity(String name) {
        return new Activity(name);
    }

    public static Action use(String name) {
        return new Activity(name);
    }

    public Activity sendStyle() {
        this.style = ">";
        return this;
    }

    public Activity receiveStyle() {
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

    public Activity label(String label) {
        this.arrowLabel = Label.label(label)
                .build();
        return this;
    }

    @Override
    public String build() {
        String activityPopulated = SIMPLE_DEFAULT_ACTIVITY_TEMPLATE.formatted(":", name, style, lineSeparator());
        if (Objects.nonNull(swimLane)) {
            if (Objects.nonNull(arrowLabel) && Objects.nonNull(note)) {
                return "|%s|%n%s%n%s%n%s".formatted(swimLane, activityPopulated, arrowLabel, note);
            }
            if (Objects.nonNull(arrowLabel)) {
                return "|%s|%n%s%n%s".formatted(swimLane, activityPopulated, arrowLabel);
            }
            if (Objects.nonNull(note)) {
                return "|%s|%n%s%n%s".formatted(swimLane, activityPopulated, note);
            }
            return "|%s|%n%s".formatted(swimLane, activityPopulated);
        }

        if (Objects.nonNull(arrowLabel) && Objects.nonNull(note)) {
            return "%s%n%s%s".formatted(activityPopulated, arrowLabel, note);
        }
        if (Objects.nonNull(arrowLabel)) {
            return "%s%n%s".formatted(activityPopulated, arrowLabel);
        }
        if (Objects.nonNull(note)) {
            return "%s%n%s".formatted(activityPopulated, note);
        }
        return activityPopulated;
    }
}
