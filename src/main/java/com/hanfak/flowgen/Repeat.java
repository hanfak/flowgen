package com.hanfak.flowgen;

import java.util.*;

/**
 * Represents the doWhile Structure
 * See https://plantuml.com/activity-diagram-beta#219cebcef334f265
 */
public class Repeat implements Action {

    private static final String REPEAT_TEMPLATE = "repeat$FIRST_ACTION%n%s%nrepeat while (%s) is (%s)%n";
    private static final String REPEAT_NO_ARROW_LABELS_TEMPLATE = "repeat$FIRST_ACTION%n%s%nrepeat while (%s)%n";
    private static final String REPEAT_WITH_LOOP_AND_EXIT_LABEL_TEMPLATE = "repeat$FIRST_ACTION%n%s%nrepeat while (%s) is (%s)%n->%s%n";
    private static final String REPEAT_WITH_EXIT_ARROW_LABEL_ONLY_TEMPLATE = "repeat$FIRST_ACTION%n%s%nrepeat while (%s)%n->%s%n";

    private final Actions actions;
    private final Action firstActivity;

    private String predicate;
    private String predicateTrueOutcome;
    private String predicateFalseOutcome;

    private Repeat(Actions actions, Action firstActivity) {
        this.actions = actions;
        this.firstActivity = firstActivity;
    }

    public static Repeat repeat() {
        return new Repeat(new Actions(), null);
    }

    public static Repeat repeat(Activity firstActivity) {
        return new Repeat(new Actions(), firstActivity);
    }

    public Repeat the(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public Repeat the(Action action) {
        this.actions.add(action);
        return this;
    }

    public Repeat and(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public Repeat and(Action action) {
        this.actions.add(action);
        return this;
    }

    public Repeat then(Action... actions) {
        this.actions.add(actions);
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

    public Repeat is(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    // Should be last action in a repeat loop
    public Repeat repeatLabel(Activity repeatLoopActivity) {
        this.actions.add(() -> "backward" + repeatLoopActivity.build());
        return this;
    }

    public Repeat leaveWhen(String predicateFalseOutcome) {
        this.predicateFalseOutcome = predicateFalseOutcome + ";";
        return this;
    }

    @Override
    public String build() {
        String allActions = actions.combineAllActions();

        String result = Optional.ofNullable(predicateFalseOutcome)
                .map(label -> bothOrExitArrowLabelOnly(allActions, label))
                .orElse(defaultOrTrueArrowOnly(allActions));
        return Optional.ofNullable(firstActivity)
                .map(a -> result.replace("$FIRST_ACTION"," " + a.build()))
                .orElse(result.replace("$FIRST_ACTION",""));
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
}
