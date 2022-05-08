package com.hanfak.flowgen;

import java.util.*;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Repeat implements Action {

    public static final String REPEAT_TEMPLATE = "repeat%n%s%nrepeat while (%s) is (%s)%n";
    public static final String REPEAT_WITH_EXIT_LABEL_TEMPLATE = "repeat%n%s%nrepeat while (%s) is (%s)%n->%s%n";
    private static final String REPEAT_WITH_EXIT_LABEL_AND_REPEAT_LABEL_TEMPLATE = "repeat%n%s%nbackward%s%nrepeat while (%s) is (%s)%n->%s%n";
    private static final String REPEAT_WITH_REPEAT_LABEL_TEMPLATE = "repeat%n%s%nbackward%s%nrepeat while (%s) is (%s)%n";
    private final Queue<Action> actions = new LinkedList<>();
    private String predicate;
    private String predicateTrueOutcome;
    private String predicateFalseOutcome;
    private String repeatLoopActivity;

    private Repeat() {
    }

    public static Repeat repeat() {
        return new Repeat();
    }

    public Repeat withActions(Action... actions) {
        this.actions.addAll(List.of(actions));
        return this;
    }

    public Repeat where(String predicate) {
        this.predicate = predicate;
        return this;
    }
    // TODO: naming - is? repeatAgainFor?
    public Repeat isTrueFor(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    public Repeat labelRepeat(Activity repeatLoopActivity) {
        this.repeatLoopActivity = repeatLoopActivity.build();
        return this;
    }

    // TODO: better name- exitLoopFor?
    public Repeat  exitLabel(String predicateFalseOutcome) {
        this.predicateFalseOutcome = predicateFalseOutcome + ";";
        return this;
    }

    @Override
    public String build() {
        String allActions= getActivitiesString(actions);
        if (Objects.isNull(repeatLoopActivity)) {
            return Optional.ofNullable(predicateFalseOutcome)
                    .map(label -> REPEAT_WITH_EXIT_LABEL_TEMPLATE.formatted(allActions, predicate, predicateTrueOutcome, label))
                    .orElse(REPEAT_TEMPLATE.formatted(allActions, predicate, predicateTrueOutcome));
        }
        return Optional.ofNullable(predicateFalseOutcome)
                .map(label -> REPEAT_WITH_EXIT_LABEL_AND_REPEAT_LABEL_TEMPLATE.formatted(allActions, repeatLoopActivity,predicate, predicateTrueOutcome, label))
                .orElse(REPEAT_WITH_REPEAT_LABEL_TEMPLATE.formatted(allActions, repeatLoopActivity,predicate, predicateTrueOutcome));
    }

    private String getActivitiesString(Queue<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
