package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Conditional implements Action {

    private static final String IF_ELSE_TEMPLATE = "if (%s) then (%s)%n%selse (%s)%n%sendif%n";
    private static final String IF_ELSE_NO_ELSE_PREDICATE_TEMPLATE = "if (%s) then (%s)%n%selse%n%sendif%n";
    private static final String IF_NO_ELSE_TEMPLATE = "if (%s) then (%s)%n%s%nendif%n";
    private static final String IF_NO_ELSE_WITH_EXIT_LABEL_TEMPLATE = "if (%s) then (%s)%n%s%nendif%n->%s%n";

    private final String predicate;

    // TODO: use type instead of List
    private final Queue<Action> thenActivity = new LinkedList<>();
    private final Queue<Action> elseActivity = new LinkedList<>();
    private String predicatePassOutcome;
    private String predicateFailOutcome;
    private String exitLabel;

    private Conditional(String predicate) {
        this.predicate = predicate;
    }

    // TODO: naming - branchWhen()?
    public static Conditional ifIsTrue(String predicate) {
        return new Conditional(predicate);
    }
// TODO: arg as builder, factory takes action,
    public Conditional then(String predicateOutcome, Action... actions) {
        this.thenActivity.addAll(List.of(actions));
        this.predicatePassOutcome = predicateOutcome;
        return this;
    }

    // TODO: and() method to chain on to then and/or orElse

    // TODO: arg as builder, factory takes action,
    public Conditional orElse(String predicateOutcome,  Action... actions) {
        this.elseActivity.addAll(List.of(actions));
        this.predicateFailOutcome = predicateOutcome;
        return this;
    }

    public Conditional orElse(Action... actions) {
        this.elseActivity.addAll(List.of(actions));
        return this;
    }

    public Conditional exitLabel(String exitLabel) {
        this.exitLabel = exitLabel+";";
        return this;
    }

    @Override
    public String build() {
        String thenActivitiesString = getActivitiesString(this.thenActivity);
        String elseActivitiesString = getActivitiesString(this.elseActivity);
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
        return IF_ELSE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, predicateFailOutcome, elseActivitiesString);
    }

    private String createIfWithNoElsePredicate(String thenActivitiesString, String elseActivitiesString) {
        return IF_ELSE_NO_ELSE_PREDICATE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, elseActivitiesString);
    }

    private String getActivitiesString(Queue<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
