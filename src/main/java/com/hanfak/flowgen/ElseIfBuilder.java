package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.Queue;

public class ElseIfBuilder {

    private final Queue<Action> actions = new LinkedList<>();
    private final String predicate;

    private String elsePredicateOutcome;
    private String thenPredicateOutcome;

    private ElseIfBuilder(String predicate) {
        this.predicate = predicate;
    }

    public static ElseIfBuilder elseIf(String predicate) {
        return new ElseIfBuilder(predicate);
    }

    public ElseIfBuilder elseLabel(String elsePredicateOutcome) {
        this.elsePredicateOutcome = elsePredicateOutcome;
        return this;
    }

    public ElseIfBuilder elseIfLabel(String thenPredicateOutcome) {
        this.thenPredicateOutcome = thenPredicateOutcome;
        return this;
    }

    public ElseIfBuilder then(Action action) {
        actions.add(action);
        return this;
    }

    public ElseIfBuilder thenDo(Action action) {
        return then(action);
    }

    public ElseIf build() {
        return new ElseIf(predicate, thenPredicateOutcome, elsePredicateOutcome, actions);
    }
}
