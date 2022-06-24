package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static java.util.stream.Collectors.joining;

public class Conditional implements Action {

    private static final String IF_ELSE_TEMPLATE = "if (%s) then (%s)%n%selse (%s)%n%sendif%n";
    private static final String IF_ELSE_WITH_EXIT_LABEL_TEMPLATE = "if (%s) then (%s)%n%selse (%s)%n%sendif%n->%s%n";
    private static final String IF_ELSE_NO_ELSE_PREDICATE_TEMPLATE = "if (%s) then (%s)%n%selse%n%sendif%n";
    private static final String IF_NO_ELSE_TEMPLATE = "if (%s) then (%s)%n%s%nendif%n";
    private static final String IF_NO_ELSE_WITH_EXIT_LABEL_TEMPLATE = "if (%s) then (%s)%n%s%nendif%n->%s%n";

    private final String predicate;

    private final Actions thenActivity;
    private final Actions elseActivity;
    private String predicatePassOutcome;
    private String predicateFailOutcome;
    private String exitLabel;

    private Conditional(String predicate, Actions thenActivity, Actions elseActivity) {
        this.predicate = predicate;
        this.thenActivity = thenActivity;
        this.elseActivity = elseActivity;
    }

    public static Conditional ifIsTrue(String predicate) {
        return new Conditional(predicate, new Actions(), new Actions());
    }

    public static Conditional branchWhen(String predicate) {
        return new Conditional(predicate, new Actions(), new Actions());
    }

    public Conditional then(ThenBuilder thenBuilder) {
        Then then = thenBuilder.build();
        this.thenActivity.add(then.actions().toArray(Action[]::new));
        this.predicatePassOutcome = then.predicateOutcome();
        return this;
    }

    public Conditional thenFor(String predicateOutcome, Action... actions) {
        this.thenActivity.add(actions);
        this.predicatePassOutcome = predicateOutcome;
        return this;
    }

    public Conditional orElse(ElseBuilder elseBuilder) {
        Else anElse = elseBuilder.build();
        this.elseActivity.add(anElse.actions().toArray(Action[]::new));
        this.predicateFailOutcome = anElse.predicateOutcome();
        return this;
    }

    public Conditional orElseFor(String predicateOutcome, Action... actions) {
        this.elseActivity.add(actions);
        this.predicateFailOutcome = predicateOutcome;
        return this;
    }

    public Conditional orElseFor(Action... actions) {
        this.elseActivity.add(actions);
        return this;
    }

    public Conditional exitLabel(String exitLabel) {
        this.exitLabel = exitLabel+";";
        return this;
    }

    @Override
    public String build() {
        String thenActivitiesString = thenActivity.combineAllActions();
        String elseActivitiesString = elseActivity.combineAllActions();
        if (elseActivitiesString.isEmpty()) {
            return createIfOnly(thenActivitiesString);
        }
        return Optional.ofNullable(predicateFailOutcome)
                .map(x -> createIfElse(thenActivitiesString, elseActivitiesString))
                .orElse(createIfWithNoElsePredicate(thenActivitiesString, elseActivitiesString));
    }

    private String createIfOnly(String thenActivitiesString) {
        return Optional.ofNullable(exitLabel)
                .map(label -> IF_NO_ELSE_WITH_EXIT_LABEL_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, label))
                .orElse(IF_NO_ELSE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString));
    }

    private String createIfElse(String thenActivitiesString, String elseActivitiesString) {
        return Optional.ofNullable(exitLabel)
                .map(label -> IF_ELSE_WITH_EXIT_LABEL_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, predicateFailOutcome, elseActivitiesString, label))
                .orElse(IF_ELSE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, predicateFailOutcome, elseActivitiesString));
    }

    private String createIfWithNoElsePredicate(String thenActivitiesString, String elseActivitiesString) {
        return IF_ELSE_NO_ELSE_PREDICATE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, elseActivitiesString);
    }

    public static class ThenBuilder {

        private final Queue<Action> actions = new LinkedList<>();
        private final String predicateOutcome;

        private ThenBuilder(String predicateOutcome) {
            this.predicateOutcome = predicateOutcome;
        }

        public static ThenBuilder forValue(String predicateOutcome) {
            return new ThenBuilder(predicateOutcome);
        }

        public ThenBuilder then(Action action) {
            actions.add(action);
            return this;
        }

        public ThenBuilder and(Action action) {
            actions.add(action);
            return this;
        }

        public Then build() {
            return new Then(predicateOutcome, actions);
        }
    }

    private static record Then(String predicateOutcome, Queue<Action> actions) { }

    public static class ElseBuilder {
        private final Queue<Action> actions = new LinkedList<>();
        private String predicateOutcome;

        private ElseBuilder(Action action) {
            this.actions.add(action);
        }

        public static ElseBuilder then(Action action) {
            return new ElseBuilder(action);
        }

        public ElseBuilder and(Action action) {
            actions.add(action);
            return this;
        }

        public ElseBuilder forValue(String predicateOutcome) {
            this.predicateOutcome = predicateOutcome;
            return this;
        }

        public Else build() {// TODO: P1 handle no predicateOutcome
            return new Else(predicateOutcome, actions);
        }
    }

    private static record Else(String predicateOutcome, Queue<Action> actions) { }
}
