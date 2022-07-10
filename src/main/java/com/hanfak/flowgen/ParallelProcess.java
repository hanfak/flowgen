package com.hanfak.flowgen;

import java.util.Optional;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
/*
* This action creates a ForkNode that splits a flow into multiple concurrent flows.
* */
public class ParallelProcess implements Action {

    private final ActionGroup actionGroup;
    private String style = "fork";
    private String endForkLabel;

    public ParallelProcess(ActionGroup actionGroup) {
        this.actionGroup = actionGroup;
    }

    public static ParallelProcess doInParallel() {
        return new ParallelProcess(new ActionGroup());
    }

    public static ParallelProcess andDoInParallel() {
        return new ParallelProcess(new ActionGroup());
    }

    public ParallelProcess the(Action... actions) {
        this.actionGroup.add(actions);
        return this;
    }

    public ParallelProcess and(Action... action) {
        this.actionGroup.add(action);
        return this;
    }

    public ParallelProcess an(Action... action) {
        this.actionGroup.add(action);
        return this;
    }

    public ParallelProcess merge() {
        this.style = "merge";
        return this;
    }

    /*
     * A join node synchronizes multiple flows.
     * By default a join, is "and" by default, so all incoming actions must all be evaluated
     * But the logic could be different,
     *  ie only need two out of three actions to join and carry on the flow,
     *  ie Only need one or the others to join  and carry on the flow,
     * */
    public ParallelProcess joinLabel(String endLabel) {
        this.endForkLabel = endLabel;
        return this;
    }

    @Override
    public String build() {
        String FORK_TEMPLATE = "fork%n%s%nend %s$LABEL$%n";
        return Optional.ofNullable(endForkLabel)
                .map(x -> FORK_TEMPLATE.replace("$LABEL$", " (%s)".formatted(endForkLabel)))
                .orElse(FORK_TEMPLATE.replace("$LABEL$", ""))
                .formatted(getActivitiesString(), style);
    }

    private String getActivitiesString() {
        return actionGroup.combineGroupActions()
                .stream()
                .collect(joining("fork again" + lineSeparator()));
    }
}
