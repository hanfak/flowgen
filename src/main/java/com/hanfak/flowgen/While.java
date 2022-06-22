package com.hanfak.flowgen;

import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class While implements Action {

    private static final String WHILE_TEMPLATE = "while (%s)%n%s%nend while%n";
    private static final String WHILE_WITH_EXIT_LABELS_TEMPLATE = "while (%s)%n%s%nend while (%s)%n";
    private static final String WHILE_WITH_LOOP_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while%n";
    private static final String WHILE_WITH_LOOP_AND_EXIT_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while (%s)%n";

    private final String predicate;

    private final Actions actions;
    private String predicateTrueOutcome;
    private String predicateFalseOutcome;

    private While(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static While loopWhen(String predicate) {
        return new While(predicate, new Actions());
    }

    public While execute(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public While and(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public While then(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public While execute(Action action) {
        this.actions.add(action);
        return this;
    }

    public While and(Action action) {
        this.actions.add(action);
        return this;
    }

    public While then(Action action) {
        this.actions.add(action);
        return this;
    }

    public While isTrueFor(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    public While exitLabel(String predicateFalseOutcome) {
        this.predicateFalseOutcome = predicateFalseOutcome;
        return this;
    }

    @Override
    public String build() {
        String allActions = actions.combineAllActions();

        if (predicateFalseOutcome != null && predicateTrueOutcome != null) {
            return WHILE_WITH_LOOP_AND_EXIT_LABELS_TEMPLATE.formatted(predicate, predicateTrueOutcome, allActions, predicateFalseOutcome);
        }

        if (predicateTrueOutcome != null) {
            return WHILE_WITH_LOOP_LABELS_TEMPLATE.formatted(predicate, predicateTrueOutcome,  allActions);
        }

        if (predicateFalseOutcome != null) {
            return WHILE_WITH_EXIT_LABELS_TEMPLATE.formatted(predicate, allActions, predicateFalseOutcome);
        }

        return WHILE_TEMPLATE.formatted(predicate, allActions);
    }
}
