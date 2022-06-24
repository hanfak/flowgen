package com.hanfak.flowgen;

public class ActivityBuilder implements Action {
    // TODO: change to ActionBuilder
    private final Actions actions;

    private ActivityBuilder(Actions actions) {
        this.actions = actions;
    }

    public static ActivityBuilder an(Activity activity) {
        Actions actions = new Actions();
        actions.add(activity);
        return new ActivityBuilder(actions);
    }

    public static ActivityBuilder following(Activity activity) {
        Actions actions = new Actions();
        actions.add(activity);
        return new ActivityBuilder(actions);
    }

    public static ActivityBuilder first(Activity activity) {
        Actions actions = new Actions();
        actions.add(activity);
        return new ActivityBuilder(actions);
    }

    public ActivityBuilder then(Activity activity) {
        actions.add(activity);
        return this;
    }

    public ActivityBuilder and(Activity activity) {
        actions.add(activity);
        return this;
    }

    @Override
    public String build() {
        return actions.combineAllActions();
    }
}