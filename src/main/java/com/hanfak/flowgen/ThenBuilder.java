package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.Queue;

public class ThenBuilder {

    private final Queue<Action> actions = new LinkedList<>();
    private final String predicateOutcome;

    private ThenBuilder(String predicateOutcome) {
        this.predicateOutcome = predicateOutcome;
    }

    public static ThenBuilder forValue(String predicateOutcome) {
        return new ThenBuilder(predicateOutcome);
    }

    public ThenBuilder then(Action action) {
        actions.add(action);
        return this;
    }

    public ThenBuilder and(Action action) {
        return then(action);
    }

    public Then build() {
        return new Then(predicateOutcome, actions);
    }
}
