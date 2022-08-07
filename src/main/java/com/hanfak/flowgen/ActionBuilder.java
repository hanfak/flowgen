package com.hanfak.flowgen;

import static com.hanfak.flowgen.Activity.activity;

public class ActionBuilder implements Action {

    private final Actions actions;

    private ActionBuilder(Actions actions) {
        this.actions = actions;
    }

    public static ActionBuilder an(Action action) {
        Actions actions = new Actions();
        actions.add(action);
        return new ActionBuilder(actions);
    }

    public static ActionBuilder an(String action) {
        Actions actions = new Actions();
        actions.add(activity(action));
        return new ActionBuilder(actions);
    }

    public static ActionBuilder doActivity(String action) {
        return an(action);
    }

    public static ActionBuilder doActivity(Action action) {
        return an(action);
    }

    public static ActionBuilder doA(String action) {
        return an(action);
    }
    public static ActionBuilder doA(Action action) {
        return an(action);
    }

    public static ActionBuilder doThe(String action) {
        return an(action);
    }

    public static ActionBuilder doThe(Action action) {
        return an(action);
    }

    public static ActionBuilder doAn(Action action) {
        return an(action);
    }

    public static ActionBuilder doAn(String action) {
        return an(action);
    }

    public static ActionBuilder following(Action action) {
        return an(action);
    }

    public static ActionBuilder following(String action) {
        return an(action);
    }

    public static ActionBuilder next(Action action) {
        return an(action);
    }

    public static ActionBuilder next(String action) {
        return an(action);
    }

    public static ActionBuilder first(Action action) {
        return an(action);
    }

    public static ActionBuilder first(String action) {
        return an(action);
    }

    public ActionBuilder then(Action action) {
        actions.add(action);
        return this;
    }

    public ActionBuilder then(String action) {
        actions.add(activity(action));
        return this;
    }

    public ActionBuilder and(Action action) {
        actions.add(action);
        return this;
    }

    public ActionBuilder and(String action) {
        actions.add(activity(action));
        return this;
    }

    @Override
    public String build() {
        return actions.combineAllActions();
    }
}