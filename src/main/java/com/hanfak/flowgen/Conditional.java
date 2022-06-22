package com.hanfak.flowgen;

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

    public Conditional then(String predicateOutcome, Action... actions) {
        this.thenActivity.add(actions);
        this.predicatePassOutcome = predicateOutcome;
        return this;
    }

    public Conditional orElse(String predicateOutcome,  Action... actions) {
        this.elseActivity.add(actions);
        this.predicateFailOutcome = predicateOutcome;
        return this;
    }

    public Conditional orElse(Action... actions) {
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
        return IF_ELSE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, predicateFailOutcome, elseActivitiesString);
    }

    private String createIfWithNoElsePredicate(String thenActivitiesString, String elseActivitiesString) {
        return IF_ELSE_NO_ELSE_PREDICATE_TEMPLATE.formatted(predicate, predicatePassOutcome, thenActivitiesString, elseActivitiesString);
    }
}
