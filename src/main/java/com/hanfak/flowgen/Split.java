package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Split implements Action {

    private final Queue<List<Action>> actions = new LinkedList<>();

    public static Split split() {
        return new Split();
    }

    public Split andDo(Action action) {
        this.actions.add(List.of(action));
        return this;
    }

    public Split andDo(Action... actions) {
        this.actions.addAll(List.of(List.of(actions)));
        return this;
    }

    @Override
    public String build() {
        String SPLIT_TEMPLATE = "split%n%s%nend split%n";
        return SPLIT_TEMPLATE.formatted(getActivitiesString());
    }

    private String getActivitiesString() {
        return actions.stream()
                .map(actions -> actions.stream()
                        .map(Action::build)
                        .collect(joining(lineSeparator())))
                .collect(joining("split again" + lineSeparator()));
    }
}
