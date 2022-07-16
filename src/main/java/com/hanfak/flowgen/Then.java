package com.hanfak.flowgen;

import java.util.Objects;
import java.util.Queue;

final class Then {

    private final String predicateOutcome;
    private final Queue<Action> actions;

    Then(String predicateOutcome, Queue<Action> actions) {
        this.predicateOutcome = predicateOutcome;
        this.actions = actions;
    }

    public String predicateOutcome() {
        return predicateOutcome;
    }

    public Queue<Action> actions() {
        return actions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Then) obj;
        return Objects.equals(this.predicateOutcome, that.predicateOutcome) &&
                Objects.equals(this.actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicateOutcome, actions);
    }
}
