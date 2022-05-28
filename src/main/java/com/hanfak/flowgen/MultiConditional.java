package com.hanfak.flowgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

// TODO: WIP
public class MultiConditional implements Action {

    /// NOTE: Storing the elseIf contents in queue<String> which is the
    private final String predicate;

    private final Queue<String> actionStrings = new LinkedList<>();

    private MultiConditional(String predicate) {
        this.predicate = predicate;
    }

    public static MultiConditional multiIf(String predicate) {
        return new MultiConditional(predicate);
    }

    public MultiConditional then(String thenPredicateOutcome, Action... actions) {
        String thenTemplate = "then (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        actionStrings.add(thenTemplate.formatted(thenPredicateOutcome, activitiesString));
        return this;
    }

    // TODO: could use builder? types - can add optional param of config?
    public MultiConditional elseIf(String elsePredicateOutcome, String predicate, String thenPredicateOutcome, Action... actions) {
        String elseIfTemplate = "(%s) elseif (%s) then (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        actionStrings.add(elseIfTemplate.formatted(elsePredicateOutcome, predicate, thenPredicateOutcome, activitiesString));
        return this;
    }

    // TODO: To overload will need types for Predicate, elsePredicateOutcome, thenPredicateOutcome
    public MultiConditional elseIf(String predicate, String thenPredicateOutcome, Action... actions) {
        return this;
    }


    MultiConditional orElse(String predicateOutcome, Action... actions) {
        String elseWithPredicateOutcomeTemplate = "else (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        actionStrings.add(elseWithPredicateOutcomeTemplate.formatted(predicateOutcome, activitiesString));
        return this;
    }

    MultiConditional orElse(Action... actions) {
        String elseWithoutPredicateOutcomeTemplate = "else%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        actionStrings.add(elseWithoutPredicateOutcomeTemplate.formatted(activitiesString));
        return this;
    }

    @Override
    public String build() {
        return "%s%sendif\n".formatted("if (%s) ".formatted(predicate), String.join("", this.actionStrings));
    }

    private String getActivitiesString(List<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
