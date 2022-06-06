package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Group implements Action {

    private final String name;
    private final Queue<Action> actions = new LinkedList<>();

    public Group(String name) {
        this.name = name;
    }

    public static Group group(String name) {
        return new Group(name);
    }

    public static Group group() {
        return new Group(null);
    }

    public Group with(Action action) {
        this.actions.add(action);
        return this;
    }

    public Group and(Action action) {
        this.actions.add(action);
        return this;
    }

    public Group last(Action action) {
        this.actions.add(action);
        return this;
    }

    public Group containing(Action... actions) {
        this.actions.addAll(List.of(actions));
        return this;
    }

    @Override
    public String build() {
        if (Objects.nonNull(name)) {
            return "group %s%n%send group%n".formatted(name, getActivitiesString(actions));
        }
        return "group%n%send group%n".formatted(getActivitiesString(actions));
    }

    private String getActivitiesString(Queue<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
