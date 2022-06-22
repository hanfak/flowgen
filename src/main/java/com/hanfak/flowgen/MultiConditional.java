package com.hanfak.flowgen;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class MultiConditional implements Action {

    /// NOTE: Storing the elseIf contents in queue<String> which is the
    private final String predicate;

    private final Actions actions;

    private MultiConditional(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static MultiConditional multiIf(String predicate) {
        return new MultiConditional(predicate, new Actions());
    }

    public MultiConditional then(String thenPredicateOutcome, Action... actions) {
        String thenTemplate = "then (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        this.actions.add(() -> thenTemplate.formatted(thenPredicateOutcome, activitiesString));
        return this;
    }

    public MultiConditional elseIf(String elsePredicateOutcome, String predicate, String thenPredicateOutcome, Action... actions) {
        String elseIfTemplate = "(%s) elseif (%s) then (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        this.actions.add(() -> elseIfTemplate.formatted(elsePredicateOutcome, predicate, thenPredicateOutcome, activitiesString));
        return this;
    }

    public MultiConditional elseIf(String predicate, String thenPredicateOutcome, Action... actions) {
        return this;
    }

    public MultiConditional orElse(String predicateOutcome, Action... actions) {
        String elseWithPredicateOutcomeTemplate = "else (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        this.actions.add(() -> elseWithPredicateOutcomeTemplate.formatted(predicateOutcome, activitiesString));
        return this;
    }

//    public MultiConditional orElse(Action... actions) {
//        String elseWithoutPredicateOutcomeTemplate = "else%n%s%n";
//        String activitiesString = getActivitiesString(List.of(actions));
//        this.actions.add(() -> elseWithoutPredicateOutcomeTemplate.formatted(activitiesString));
//        return this;
//    }

    @Override
    public String build() {
        return "%s%sendif%n".formatted("if (%s) ".formatted(predicate), actions.combineAllActions());
    }

    private String getActivitiesString(List<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
