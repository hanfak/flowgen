package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;
import static java.util.Optional.ofNullable;

public class Conditional implements Action {

    private static final String IF_ELSE_GENERAL_TEMPLATE = "if ($PREDICATE_LOGIC) then$PREDICATE_PASS$THEN_ACTIONS$else$PREDICATE_FAIL$ELSE_ACTIONS$endif$EXIT_LABEL" + lineSeparator();
    private static final String GUARD_CLAUSE_RETURN_EARLY_TEMPLATE = "if ($PREDICATE_LOGIC) then$PREDICATE_PASS$THEN_ACTIONS$$PREDICATE_FAILendif$EXIT_LABEL" + lineSeparator();

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

    public static Conditional ifIs(String predicate) {
        return new Conditional(predicate, new Actions(), new Actions());
    }

    public static Conditional ifThe(String predicate) {
        return new Conditional(predicate, new Actions(), new Actions());
    }

    public static Conditional ifIt(String predicate) {
        return new Conditional(predicate, new Actions(), new Actions());
    }

    public static Conditional ifWhen(String predicate) {
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

    public Conditional then(Action... actions) {
        this.thenActivity.add(actions);
        return this;
    }

    public Conditional thenFor(String predicateOutcome, Action... actions) {
        this.thenActivity.add(actions);
        this.predicatePassOutcome = predicateOutcome;
        return this;
    }

    public Conditional or(ElseBuilder elseBuilder) {
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

    public Conditional elseLabel(String predicateFailOutcome) {
        this.predicateFailOutcome = predicateFailOutcome;
        return this;
    }

    public Conditional existLabel(String exitLabel) {
        this.exitLabel = exitLabel+";";
        return this;
    }

    @Override
    public String build() {
        String thenActivitiesString = thenActivity.combineAllActions();
        String elseActivitiesString = elseActivity.combineAllActions();
        String predicatePassString = ofNullable(predicatePassOutcome).map(" (%s)%n"::formatted).orElse(lineSeparator());
        String exitLabelString = ofNullable(exitLabel).map("%n->%s"::formatted).orElse("");
        if (!elseActivitiesString.isEmpty()) {
            String predicateFailString = ofNullable(predicateFailOutcome).map(" (%s)%n"::formatted).orElse("");
            return IF_ELSE_GENERAL_TEMPLATE
                    .replace("$PREDICATE_LOGIC", predicate)
                    .replace("$PREDICATE_PASS", predicatePassString)
                    .replace("$THEN_ACTIONS$", thenActivitiesString)
                    .replace("$PREDICATE_FAIL", predicateFailString)
                    .replace("$ELSE_ACTIONS$",  lineSeparator() + elseActivitiesString)
                    .replace("$EXIT_LABEL", exitLabelString);
        }
        return GUARD_CLAUSE_RETURN_EARLY_TEMPLATE
                .replace("$PREDICATE_LOGIC", predicate)
                .replace("$PREDICATE_PASS", predicatePassString)
                .replace("$THEN_ACTIONS$", thenActivitiesString)
                .replace("$PREDICATE_FAIL", ofNullable(predicateFailOutcome).map("else (%s)%n"::formatted).orElse(""))
                .replace("$EXIT_LABEL", exitLabelString);
    }
}
