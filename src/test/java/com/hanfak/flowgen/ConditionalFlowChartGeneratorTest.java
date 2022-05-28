package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Conditional.conditional;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

class ConditionalFlowChartGeneratorTest {

    // TODO: No predicate outcome show for then branch
    // TODO: then without predicateOutcome
    // TODO: styling - diamond, line, colour
    @Test
    void ifELseWithPredicatesOnBothPaths() {
        String flowchart = flowchart()
                .then(conditional("is big?")
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
    void elsePathHasNoPredicateDefined() {
        String flowchart = flowchart()
                .then(conditional("is big?")
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
                .then(conditional("is big?")
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
                .then(conditional("is big?")
                        .then("yes", doActivity("action1"), doActivity("action3"))
                        .exitLabel("NOK")
                )
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                if (is big?) then (yes)
                :action1;
                :action3;
                endif
                ->NOK;
                @enduml""");
    }

    @Test
    void nestedIf() {
        String flowchart = flowchart()
                .then(conditional("is big?")
                        .then("yes", doActivity("action1"))
                        .orElse("no",
                                conditional("is tiny?")
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
