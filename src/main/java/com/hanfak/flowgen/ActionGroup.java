package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class ActionGroup {

    private final Queue<List<Action>> actionsGroup = new LinkedList<>();

    void add(Action... action) {
        actionsGroup.add(List.of(action));
    }

    List<String> combineGroupActions() {
        return actionsGroup.stream()
                .map(action -> action.stream()
                        .map(Action::build)
                        .collect(joining(lineSeparator())))
                .collect(toList());
    }
}
