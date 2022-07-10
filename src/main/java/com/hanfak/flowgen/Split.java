package com.hanfak.flowgen;

import static com.hanfak.flowgen.HiddenActivity.hiddenActivity;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Split implements Action {

    private final ActionGroup actionGroup;

    public Split(ActionGroup actionGroup) {
        this.actionGroup = actionGroup;
    }

    public static Split split() {
        return new Split(new ActionGroup());
    }

    public Split andDo(Action... actions) {
        this.actionGroup.add(actions);
        return this;
    }

    public Split andDoWith(Action... actions) {
        this.actionGroup.add(actions);
        return this;
    }

    public Split andDoStart(Action action) {
        this.actionGroup.add(hiddenActivity(action.build()));
        return this;
    }

    @Override
    public String build() {
        String SPLIT_TEMPLATE = "split%n%s%nend split%n";
        return SPLIT_TEMPLATE.formatted(getActivitiesString());
    }

    private String getActivitiesString() {
        return actionGroup.combineGroupActions().stream()
                .collect(joining("split again" + lineSeparator()));
    }
}
