package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class ParallelProcess implements Action {

    private final ActionGroup actionGroup;

    public ParallelProcess(ActionGroup actionGroup) {
        this.actionGroup = actionGroup;
    }

    public static ParallelProcess doInParallel() {
        return new ParallelProcess(new ActionGroup());
    }

    public ParallelProcess the(Action action) {
        this.actionGroup.add(action);
        return this;
    }

    public ParallelProcess the(Action... actions) {
        this.actionGroup.add(actions);
        return this;
    }

    @Override
    public String build() {
        String FORK_TEMPLATE = "fork%n%s%nend fork%n";
        return FORK_TEMPLATE.formatted(getActivitiesString());
    }

    private String getActivitiesString() {
        return actionGroup.combineGroupActions().stream()
                .collect(joining("fork again" + lineSeparator()));
    }
}
