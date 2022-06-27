package com.hanfak.flowgen;

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

    public static ActionBuilder doAn(Action action) {
        Actions actions = new Actions();
        actions.add(action);
        return new ActionBuilder(actions);
    }

    public static ActionBuilder following(Action action) {
        Actions actions = new Actions();
        actions.add(action);
        return new ActionBuilder(actions);
    }

    public static ActionBuilder next(Action action) {
        Actions actions = new Actions();
        actions.add(action);
        return new ActionBuilder(actions);
    }

    public static ActionBuilder first(Action action) {
        Actions actions = new Actions();
        actions.add(action);
        return new ActionBuilder(actions);
    }

    public ActionBuilder then(Action action) {
        actions.add(action);
        return this;
    }

    public ActionBuilder and(Action action) {
        actions.add(action);
        return this;
    }

    @Override
    public String build() {
        return actions.combineAllActions();
    }
}