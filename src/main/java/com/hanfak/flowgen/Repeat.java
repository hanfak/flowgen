package com.hanfak.flowgen;

import java.util.*;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

/**
 * Represents the doWhile Structure
 * See https://plantuml.com/activity-diagram-beta#219cebcef334f265
 */
public class Repeat implements Action {

    private static final String REPEAT_TEMPLATE = "repeat%n%s%nrepeat while (%s) is (%s)%n";
    private static final String REPEAT_NO_ARROW_LABELS_TEMPLATE = "repeat%n%s%nrepeat while (%s)%n";
    private static final String REPEAT_WITH_LOOP_AND_EXIT_LABEL_TEMPLATE = "repeat%n%s%nrepeat while (%s) is (%s)%n->%s%n";
    private static final String REPEAT_WITH_EXIT_ARROW_LABEL_ONLY_TEMPLATE = "repeat%n%s%nrepeat while (%s)%n->%s%n";

    private final Queue<Action> actions = new LinkedList<>();
    private String predicate;
    private String predicateTrueOutcome;
    private String predicateFalseOutcome;

    private Repeat() {
    }

    public static Repeat repeat() {
        return new Repeat();
    }

    public Repeat the(Action... actions) {
        this.actions.addAll(List.of(actions));
        return this;
    }

    public Repeat the(Action action) {
        this.actions.add(action);
        return this;
    }

    public Repeat and(Action... actions) {
        this.actions.addAll(List.of(actions));
        return this;
    }

    public Repeat and(Action action) {
        this.actions.add(action);
        return this;
    }

    public Repeat then(Action... actions) {
        this.actions.addAll(List.of(actions));
        return this;
    }

    public Repeat then(Action action) {
        this.actions.add(action);
        return this;
    }

    public Repeat repeatWhen(String predicate) {
        this.predicate = predicate;
        return this;
    }

    public Repeat repeatWhen(String predicate, String predicateTrueOutcome) {
        this.predicate = predicate;
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    public Repeat repeatWhen(String predicate, String predicateTrueOutcome, String predicateFalseOutcome) {
        this.predicate = predicate;
        this.predicateTrueOutcome = predicateTrueOutcome;
        this.predicateFalseOutcome = predicateFalseOutcome + ";";
        return this;
    }

    // TODO: naming - is? repeatAgainFor?
    public Repeat isTrueFor(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    // TODO: naming repeatLoopAction
    public Repeat labelRepeat(Action repeatLoopActivity) {
        this.actions.add(() -> "backward" + repeatLoopActivity.build());
        return this;
    }

    // TODO: better name- exitLoopFor?
    public Repeat exitOn(String predicateFalseOutcome) {
        this.predicateFalseOutcome = predicateFalseOutcome + ";";
        return this;
    }

    @Override
    public String build() {
        String allActions = getActivitiesString(actions);
        return Optional.ofNullable(predicateFalseOutcome)
                .map(label -> bothOrExitArrowLabelOnly(allActions, label))
                .orElse(defaultOrTrueArrowOnly(allActions));
    }

    private String bothOrExitArrowLabelOnly(String allActions, String label) {
        if (Objects.isNull(predicateTrueOutcome)) {
            return REPEAT_WITH_EXIT_ARROW_LABEL_ONLY_TEMPLATE.formatted(allActions, predicate, label);
        }
        return REPEAT_WITH_LOOP_AND_EXIT_LABEL_TEMPLATE.formatted(allActions, predicate, predicateTrueOutcome, label);
    }

    private String defaultOrTrueArrowOnly(String allActions) {
        if (Objects.isNull(predicateTrueOutcome)) {
            return REPEAT_NO_ARROW_LABELS_TEMPLATE.formatted(allActions, predicate);
        }
        return REPEAT_TEMPLATE.formatted(allActions, predicate, predicateTrueOutcome);
    }

    private String getActivitiesString(Queue<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
