package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

class Actions {

    private final Queue<Action> actions = new LinkedList<>();

    void add(Action... action) {
        actions.addAll(List.of(action));
    }

    String combineAllActions() {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()))
                .replaceAll("(?m)^[ \t]*\r?\n", "");
    }
}
