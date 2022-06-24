package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Conditional.ElseBuilder.then;
import static com.hanfak.flowgen.Conditional.ThenBuilder.forValue;
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
                        .then("yes", doActivity("action1"), andActivity("action3"))
                        .orElse("no", doActivity("action2")))
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
                .then(ifIsTrue("is big?")
                        .then(forValue("yes")
                                .then( doActivity("action1"))
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
                        .then("yes", doActivity("action1"), andActivity("action3"))
                        .orElse("no", doActivity("action2"), andExit()))
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
                        .then("yes", doActivity("action1"), thenActivity("action3"), andActivity("action4"))
                        .orElse(doActivity("action2")))
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
                        .then("yes", doActivity("action1"), doActivity("action3")))
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
                        .then("yes", doActivity("action1"), doActivity("action3"))
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
                        .then("yes", doActivity("action1"), doActivity("action3"))
                        .orElse("no", doActivity("action2"))
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
                endif
                ->NOK;
                :action4;
                @enduml""");
    }

    @Test
    void nestedIf() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .then("yes", doActivity("action1"))
                        .orElse("no",
                                ifIsTrue("is tiny?")
                                        .then("yes", doActivity("action2"))
                                        .orElse("no", doActivity("action3")),
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
