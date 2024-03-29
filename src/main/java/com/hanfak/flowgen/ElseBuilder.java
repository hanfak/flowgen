package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.Queue;

public class ElseBuilder {

    private final Queue<Action> actions = new LinkedList<>();
    private String predicateOutcome;

    private ElseBuilder(Action action) {
        this.actions.add(action);
    }

    public static ElseBuilder elseDo(Action action) {
        return new ElseBuilder(action);
    }

    public ElseBuilder and(Action action) {
        actions.add(action);
        return this;
    }

    public ElseBuilder forValue(String predicateOutcome) {
        this.predicateOutcome = predicateOutcome;
        return this;
    }

    public Else build() {
        return new Else(predicateOutcome, actions);
    }
}
