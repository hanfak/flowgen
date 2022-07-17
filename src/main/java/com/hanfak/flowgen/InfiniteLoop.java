package com.hanfak.flowgen;

import java.util.Optional;

import static java.lang.String.format;

public class InfiniteLoop implements Action {

    private static final String WHILE_TEMPLATE = "while (%s)%n%s%nend while%n-[hidden]->%ndetach%n";
    private static final String WHILE_WITH_LOOP_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while%n-[hidden]->%ndetach%n";

    private final String predicate;

    private final Actions actions;
    private String predicateTrueOutcome;
    private Note note;

    private InfiniteLoop(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static InfiniteLoop infiniteLoopWhen(String predicate) {
        return new InfiniteLoop(predicate, new Actions());
    }

    public InfiniteLoop with(Note note) {
        this.note = note;
        return this;
    }

    public InfiniteLoop execute(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public InfiniteLoop perform(Action... actions) {
        return execute(actions);
    }

    public InfiniteLoop and(Action... actions) {
        return execute(actions);
    }

    public InfiniteLoop then(Action... actions) {
        return execute(actions);
    }

    public InfiniteLoop is(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    public InfiniteLoop repeatLabel(Activity repeatLoopActivity) {
        this.actions.add(() -> "backward" + repeatLoopActivity.build());
        return this;
    }

    @Override
    public String build() {
        String allActionsCombined = actions.combineAllActions();
        String allActions = Optional.ofNullable(note)
                .map(aNote -> note.build() + allActionsCombined)
                .orElse(allActionsCombined);
        return Optional.ofNullable(predicateTrueOutcome)
                .map(label -> format(WHILE_WITH_LOOP_LABELS_TEMPLATE, predicate, label,  allActions))
                .orElse(format(WHILE_TEMPLATE, predicate, allActions));
    }
}
