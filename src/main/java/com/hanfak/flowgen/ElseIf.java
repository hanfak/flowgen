package com.hanfak.flowgen;

import java.util.Objects;
import java.util.Queue;

final class ElseIf {

    private final String predicate;
    private final String thenPredicateOutcome;
    private final String elsePredicateOutcome;
    private final Queue<Action> actions;

    ElseIf(String predicate, String thenPredicateOutcome, String elsePredicateOutcome, Queue<Action> actions) {
        this.predicate = predicate;
        this.thenPredicateOutcome = thenPredicateOutcome;
        this.elsePredicateOutcome = elsePredicateOutcome;
        this.actions = actions;
    }

    public String predicate() {
        return predicate;
    }

    public String thenPredicateOutcome() {
        return thenPredicateOutcome;
    }

    public String elsePredicateOutcome() {
        return elsePredicateOutcome;
    }

    public Queue<Action> actions() {
        return actions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElseIf) obj;
        return Objects.equals(this.predicate, that.predicate) &&
                Objects.equals(this.thenPredicateOutcome, that.thenPredicateOutcome) &&
                Objects.equals(this.elsePredicateOutcome, that.elsePredicateOutcome) &&
                Objects.equals(this.actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicate, thenPredicateOutcome, elsePredicateOutcome, actions);
    }
}