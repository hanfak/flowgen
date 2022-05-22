package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.activity;
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
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), activity("action3"))
                        .orElse("no", activity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
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
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), activity("action3"))
                        .orElse(activity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                if (is big?) then (yes)
                :action1;
                :action3;
                else
                :action2;
                endif
                @enduml""");
    }

    @Test
    void ifWithoutElse() {
        String flowchart = flowchart()
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), activity("action3")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                if (is big?) then (yes)
                :action1;
                :action3;
                endif
                @enduml""");
    }

    @Test
    void ifWithoutElseLabelExitConnector() {
        String flowchart = flowchart()
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), activity("action3"))
                        .exitLabel("NOK")
                )
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
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
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"))
                        .orElse("no",
                                conditional("is tiny?")
                                        .then("yes", activity("action2"))
                                        .orElse("no", activity("action3")),
                                activity("action4")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
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
