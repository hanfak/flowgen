package com.hanfak.flowgen;

import java.util.Optional;

public class InfiniteLoop implements Action {

    private static final String WHILE_TEMPLATE = "while (%s)%n%s%nend while%n-[hidden]->%ndetach%n";
    private static final String WHILE_WITH_LOOP_LABELS_TEMPLATE = "while (%s) is (%s)%n%s%nend while%n-[hidden]->%ndetach%n";

    private final String predicate;

    private final Actions actions;
    private String predicateTrueOutcome;

    private InfiniteLoop(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static InfiniteLoop infiniteLoopWhen(String predicate) {
        return new InfiniteLoop(predicate, new Actions());
    }

    public InfiniteLoop execute(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public InfiniteLoop and(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public InfiniteLoop then(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public InfiniteLoop execute(Action action) {
        this.actions.add(action);
        return this;
    }

    public InfiniteLoop and(Action action) {
        this.actions.add(action);
        return this;
    }

    public InfiniteLoop then(Action action) {
        this.actions.add(action);
        return this;
    }

    public InfiniteLoop isTrueFor(String predicateTrueOutcome) {
        this.predicateTrueOutcome = predicateTrueOutcome;
        return this;
    }

    @Override
    public String build() {
        String allActions = actions.combineAllActions();
        return Optional.ofNullable(predicateTrueOutcome)
                .map(label -> WHILE_WITH_LOOP_LABELS_TEMPLATE.formatted(predicate, label,  allActions))
                .orElse(WHILE_TEMPLATE.formatted(predicate, allActions));
    }
}
