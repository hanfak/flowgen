package com.hanfak.flowgen;

import java.util.Objects;

import static java.lang.String.format;

public class Group implements Action {

    private final String name;
    private final Actions actions;

    private Group(String name, Actions actions) {
        this.name = name;
        this.actions = actions;
    }

    public static Group group(String name) {
        return new Group(name, new Actions());
    }

    public static Group group() {
        return new Group(null, new Actions());
    }

    public Group with(Action... actions) {
        this.actions.add(actions);
        return this;
    }

    public Group and(Action... actions) {
        return with(actions);
    }

    public Group last(Action... actions) {
        return with(actions);
    }

    public Group containing(Action... actions) {
        return with(actions);
    }

    @Override
    public String build() {
        String allActions = actions.combineAllActions();
        if (Objects.nonNull(name)) {
            return format("group %s%n%send group%n", name, allActions);
        }
        return format("group%n%send group%n", allActions);
    }
}
