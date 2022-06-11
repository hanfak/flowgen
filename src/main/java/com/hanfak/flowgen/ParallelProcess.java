package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class ParallelProcess implements Action {

    private final Queue<List<Action>> actions = new LinkedList<>();

    public static ParallelProcess doInParallel() {
        return new ParallelProcess();
    }

    public ParallelProcess the(Action action) {
        this.actions.add(List.of(action));
        return this;
    }

    public ParallelProcess the(Action... actions) {
        this.actions.add(List.of(actions));
        return this;
    }

    @Override
    public String build() {
        String FORK_TEMPLATE = "fork%n%s%nend fork%n";
        return FORK_TEMPLATE.formatted(getActivitiesString());
    }

    private String getActivitiesString() {
        return actions.stream()
                .map(actions -> actions.stream()
                        .map(Action::build)
                        .collect(joining(lineSeparator())))
                .collect(joining("fork again" + lineSeparator()));
    }
}
