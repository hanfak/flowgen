package com.hanfak.flowgen;

import java.util.*;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;

public class MultiConditional implements Action {

    private final String predicate;
    private final Actions actions;

    private MultiConditional(String predicate, Actions actions) {
        this.predicate = predicate;
        this.actions = actions;
    }

    public static MultiConditional ifTrueFor(String predicate) {
        return new MultiConditional(predicate, new Actions());
    }

    public MultiConditional then(String thenPredicateOutcome, Action... actions) {
        String thenTemplate = "then (%s)%n%s%n";
        String activitiesString = getActivitiesString(List.of(actions));
        this.actions.add(() -> format(thenTemplate, thenPredicateOutcome, activitiesString));
        return this;
    }

    public MultiConditional then(ThenBuilder thenBuilder) {
        Then then = thenBuilder.build();
        String thenTemplate = "then (%s)%n%s%n";
        String activitiesString = then.actions().stream().map(Action::build).collect(joining(lineSeparator()));
        this.actions.add(() -> format(thenTemplate, then.predicateOutcome(), activitiesString));
        return this;
    }

    public MultiConditional then(ElseIfBuilder elseIfBuilder) {
        ElseIf elseIf = elseIfBuilder.build();
        String elseIfTemplate = "(%s) elseif (%s) then (%s)%n%s%n";
        String elseIfNoElseIfLabelTemplate = "(%s) elseif (%s) then%n%s%n";
        String elseIfNoElseLabelTemplate = "elseif (%s) then (%s)%n%s%n";
        String elseIfNoLabelTemplate = "elseif (%s) then%n%s%n";
        String activitiesString = elseIf.actions().stream().map(Action::build).collect(joining(lineSeparator()));
        if (nonNull(elseIf.elsePredicateOutcome()) && nonNull(elseIf.thenPredicateOutcome())) {
            this.actions.add(() -> format(elseIfTemplate, elseIf.elsePredicateOutcome(), elseIf.predicate(), elseIf.thenPredicateOutcome(), activitiesString));
        }
        if (nonNull(elseIf.elsePredicateOutcome()) && isNull(elseIf.thenPredicateOutcome())) {
            this.actions.add(() -> format(elseIfNoElseIfLabelTemplate, elseIf.elsePredicateOutcome(), elseIf.predicate(), activitiesString));
        }
        if (isNull(elseIf.elsePredicateOutcome()) && nonNull(elseIf.thenPredicateOutcome())) {
            this.actions.add(() -> format(elseIfNoElseLabelTemplate, elseIf.predicate(), elseIf.thenPredicateOutcome(), activitiesString));
        }
        if (isNull(elseIf.elsePredicateOutcome()) && isNull(elseIf.thenPredicateOutcome())){
            this.actions.add(() -> format(elseIfNoLabelTemplate, elseIf.predicate(), activitiesString));
        }
        return this;
    }

    public MultiConditional orElse(ElseBuilder elseBuilder) {
        Else anElse = elseBuilder.build();
        String elseWithPredicateOutcomeTemplate = "else (%s)%n%s%n";
        String elseWithNoPredicateOutcomeTemplate = "else%n%s%n";
        String activitiesString = anElse.actions().stream().map(Action::build).collect(joining(lineSeparator()));
        this.actions.add(() -> Optional.ofNullable(anElse.predicateOutcome())
                .map(outcome-> format(elseWithPredicateOutcomeTemplate, outcome, activitiesString))
                .orElse(format(elseWithNoPredicateOutcomeTemplate, activitiesString)));
        return this;
    }

    @Override
    public String build() {
        return format("%s%sendif%n", format("if (%s) ", predicate), actions.combineAllActions());
    }

    private String getActivitiesString(List<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }
}
