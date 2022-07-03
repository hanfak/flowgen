package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.ActionBuilder.doAn;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Conditional.*;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.ThenBuilder.forValue;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ConditionalFlowChartGeneratorTest {

    // TODO: P2 arrow style after than, after endif
    // TODO: P2 styling - diamond, line, colour
    @Test
    void ifELseWithPredicatesOnBothPaths() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), andActivity("action3"))
                        .orElseFor("no", doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                else (no)
                :action2;
                endif
                @enduml""");
    }

    @Test
    void ifELseWithNoPredicatesDefinedOnBothPaths() {
        String flowchart = flowchart()
                .then(ifThe("house is big?")
                        .then(doActivity("action1"))
                        .or(elseDo(activity("action2"))))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (house is big?) then
                :action1;
                else
                :action2;
                endif
                @enduml""");
    }

    @Test
    void ifELseDetachesOnIfBranch() {
        String flowchart = flowchart()
                .then(branchWhen("is big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3"))
                                .and(exit()))
                        .or(elseDo(activity("action2"))
                                .and(doActivity("action5"))
                                .forValue("no")))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                detach
                else (no)
                :action2;
                :action5;
                endif
                :action4;
                @enduml""");
    }

    @Test
    void ifELseDetachesOnElseBranch() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doAn(activity("action1")).and(activity("action3")))
                        .orElseFor("no", doActivity("action2"), andExit()))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                else (no)
                :action2;
                detach
                endif
                :action4;
                @enduml""");
    }

    @Test
    void elsePathHasNoPredicateDefined() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), thenActivity("action3"), and("action4"))
                        .orElseFor(doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                :action4;
                else
                :action2;
                endif
                @enduml""");
    }

    @Test
    void ifWithoutElse() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                endif
                @enduml""");
    }

    @Test
    void ifWithoutElseLabelExitConnector() {
        String flowchart = flowchart()
                .then(ifIt("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .existLabel("NOK"))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                endif
                ->NOK;
                :action4;
                @enduml""");
    }

    @Test
    void ifElseLabelExitConnector() {
        String flowchart = flowchart()
                .then(ifWhen("200?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .orElseFor("no", doAn(activity("action2")).and(activity("action5")))
                        .existLabel("NOK"))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (200?) then (yes)
                :action1;
                :action3;
                else (no)
                :action2;
                :action5;
                endif
                ->NOK;
                :action4;
                @enduml""");
    }

    @Test
    void nestedIf() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .then(forValue("yes").then(doActivity("action1")))
                        .orElseFor("no",
                                ifIs("tiny?")
                                        .thenFor("yes", ifIs("tiny?")
                                                .thenFor("yes", doActivity("action2"))
                                                .orElseFor("no", doActivity("action3"))
                                                .existLabel("Next 0"))
                                        .orElseFor("no", doActivity("action3"))
                                        .existLabel("Next 1"),
                                doActivity("action4"))
                        .existLabel("Next 2"))
                .then(doActivity("action5"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                else (no)
                if (tiny?) then (yes)
                if (tiny?) then (yes)
                :action2;
                else (no)
                :action3;
                endif
                ->Next 0;
                else (no)
                :action3;
                endif
                ->Next 1;
                :action4;
                endif
                ->Next 2;
                :action5;
                @enduml""");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithLabelElseBranch() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action1;
                :action3;
                else (no)
                endif
                :action2;
                @enduml""");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithLabelElseBranchAndExitLabel() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no")
                        .existLabel("next"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action1;
                :action3;
                else (no)
                endif
                ->next;
                :action2;
                @enduml""");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithNoThenLabelAndLabelElseBranchAndExitLabel() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(doAn(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no")
                        .existLabel("next"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then
                :action1;
                :action3;
                else (no)
                endif
                ->next;
                :action2;
                @enduml""");
    }
}
