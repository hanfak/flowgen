package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.ActionBuilder.doAn;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.ElseBuilder.then;
import static com.hanfak.flowgen.ThenBuilder.forValue;
import static com.hanfak.flowgen.Conditional.branchWhen;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

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
    void ifELseDetachesOnIfBranch() {
        String flowchart = flowchart()
                .then(branchWhen("is big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3"))
                                .and(exit()))
                        .orElse(then(doActivity("action2"))
                                .and(doActivity("action5"))
                                .forValue("no")))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                :action3;
                -[hidden]->
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
                -[hidden]->
                detach
                endif
                :action4;
                @enduml""");
    }

    @Test
    void elsePathHasNoPredicateDefined() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), thenActivity("action3"), andActivity("action4"))
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
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .exitLabel("NOK")
                )
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
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .orElseFor("no", doAn(activity("action2")).and(activity("action5")))
                        .exitLabel("NOK"))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
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
                        .thenFor("yes", doActivity("action1"))
                        .orElseFor("no",
                                ifIsTrue("is tiny?")
                                        .thenFor("yes", doActivity("action2"))
                                        .orElseFor("no", doActivity("action3")),
                                doActivity("action4")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (is big?) then (yes)
                :action1;
                else (no)
                if (is tiny?) then (yes)
                :action2;
                else (no)
                :action3;
                endif
                :action4;
                endif
                @enduml""");
    }
}
