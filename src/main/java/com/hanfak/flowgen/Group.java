package com.hanfak.flowgen;

import java.util.Objects;

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
        this.actions.add(actions);
        return this;
    }

    @Override
    public String build() {
        String allActions = actions.combineAllActions();
        if (Objects.nonNull(name)) {
            return "group %s%n%send group%n".formatted(name, allActions);
        }
        return "group%n%send group%n".formatted(allActions);
    }
}
