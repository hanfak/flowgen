package com.hanfak.flowgen;

import java.util.*;

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
        this.actions.add(() -> thenTemplate.formatted(thenPredicateOutcome, activitiesString));
        return this;
    }

    public MultiConditional then(ThenBuilder thenBuilder) {
        Then then = thenBuilder.build();
        String thenTemplate = "then (%s)%n%s%n";
        String activitiesString = then.actions().stream().map(Action::build).collect(joining(lineSeparator()));
        this.actions.add(() -> thenTemplate.formatted(then.predicateOutcome(), activitiesString));
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
            this.actions.add(() -> elseIfTemplate.formatted(elseIf.elsePredicateOutcome(), elseIf.predicate(), elseIf.thenPredicateOutcome(), activitiesString));
        }
        if (nonNull(elseIf.elsePredicateOutcome()) && isNull(elseIf.thenPredicateOutcome())) {
            this.actions.add(() -> elseIfNoElseIfLabelTemplate.formatted(elseIf.elsePredicateOutcome(), elseIf.predicate(), activitiesString));
        }
        if (isNull(elseIf.elsePredicateOutcome()) && nonNull(elseIf.thenPredicateOutcome())) {
            this.actions.add(() -> elseIfNoElseLabelTemplate.formatted(elseIf.predicate(), elseIf.thenPredicateOutcome(), activitiesString));
        }
        if (isNull(elseIf.elsePredicateOutcome()) && isNull(elseIf.thenPredicateOutcome())){
            this.actions.add(() -> elseIfNoLabelTemplate.formatted(elseIf.predicate(), activitiesString));
        }
        return this;
    }

    public MultiConditional orElse(ElseBuilder elseBuilder) {
        Else anElse = elseBuilder.build();
        String elseWithPredicateOutcomeTemplate = "else (%s)%n%s%n";
        String elseWithNoPredicateOutcomeTemplate = "else%n%s%n";
        String activitiesString = anElse.actions().stream().map(Action::build).collect(joining(lineSeparator()));
        this.actions.add(() -> Optional.ofNullable(anElse.predicateOutcome())
                .map(outcome-> elseWithPredicateOutcomeTemplate.formatted(outcome, activitiesString))
                .orElse(elseWithNoPredicateOutcomeTemplate.formatted(activitiesString)));
        return this;
    }

    @Override
    public String build() {
        return "%s%sendif%n".formatted("if (%s) ".formatted(predicate), actions.combineAllActions());
    }

    private String getActivitiesString(List<Action> actions) {
        return actions.stream()
                .map(Action::build)
                .collect(joining(lineSeparator()));
    }

    public static class ElseIfBuilder {

        private final Queue<Action> actions = new LinkedList<>();
        private final String predicate;

        private String elsePredicateOutcome;
        private String thenPredicateOutcome;

        private ElseIfBuilder(String predicate) {
            this.predicate = predicate;
        }

        public static ElseIfBuilder elseIf(String predicate) {
            return new ElseIfBuilder(predicate);
        }

        public ElseIfBuilder elseLabel(String elsePredicateOutcome) {
            this.elsePredicateOutcome = elsePredicateOutcome;
            return this;
        }

        public ElseIfBuilder elseIfLabel(String thenPredicateOutcome) {
            this.thenPredicateOutcome = thenPredicateOutcome;
            return this;
        }

        public ElseIfBuilder then(Action action) {
            actions.add(action);
            return this;
        }

        public ElseIfBuilder thenDo(Action action) {
            actions.add(action);
            return this;
        }

        public ElseIf build() {
            return new ElseIf(predicate,thenPredicateOutcome, elsePredicateOutcome, actions);
        }
    }

    private static record ElseIf(String predicate, String thenPredicateOutcome, String elsePredicateOutcome, Queue<Action> actions) { }

    public static class ThenBuilder {

        private final Queue<Action> actions = new LinkedList<>();
        private final String predicateOutcome;

        private ThenBuilder(String predicateOutcome) {
            this.predicateOutcome = predicateOutcome;
        }

        public static ThenBuilder forValue(String predicateOutcome) {return new ThenBuilder(predicateOutcome);}

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
