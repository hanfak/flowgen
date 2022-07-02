package com.hanfak.flowgen;

import java.util.Optional;

public class While implements Action {

    private static final String WHILE_TEMPLATE = "while (%s)%n%s%nend while%n";
    private static final String WHILE_WITH_EXIT_LABELS_TEMPLATE = "while (%s)%n%s%nend while (%s)%n";
    private static final String WHILE_WITH_LOOP_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while%n";
    private static final String WHILE_WITH_LOOP_AND_EXIT_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while (%s)%n";

    private final String predicate;
    private final Actions actions;

    private String predicateTrueOutcome;
    private String predicateFalseOutcome;
    private Note note;

    private While(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static While loopWhen(String predicate) {
        return new While(predicate, new Actions());
    }

    public While with(Note note) {
        this.note = note;
        return this;
    }

    public While execute(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public While perform(Action... actions) {
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

    public While is(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    public While leaveWhen(String predicateFalseOutcome) {
        this.predicateFalseOutcome = predicateFalseOutcome;
        return this;
    }

    public While repeatLabel(Activity repeatLoopActivity) {
        this.actions.add(() -> "backward" + repeatLoopActivity.build());
        return this;
    }

    @Override
    public String build() {
        String allActionsCombined = actions.combineAllActions();
        String allActions = Optional.ofNullable(note)
                .map(aNote -> note.build() + allActionsCombined)
                .orElse(allActionsCombined);

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
