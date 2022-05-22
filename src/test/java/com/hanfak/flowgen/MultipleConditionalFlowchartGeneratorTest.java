package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.andActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.multiConditional;
import static org.assertj.core.api.Assertions.assertThat;

class MultipleConditionalFlowchartGeneratorTest {

    // TODO: No predicate outcome show for then branch
    // TODO: No predicate outcome show for multiple then branch
    // TODO: No predicate outcome show for multiple elseif branch
    // TODO: No predicate outcome show for orelse branch
    // TODO: guard clause
    // TODO: inline styling
    @Test
    void multipleIfELseWithPredicatesOnAllPaths() {
        String flowchart = flowchart()
                .then(
                        multiConditional("big?")
                                .then("yes", activity("action"))
                                .elseIf("no", "condition 1", "yes", activity("action1"), andActivity("action3"))
                                .elseIf("no", "condition 2", "yes", activity("action2"))
                                .elseIf("no", "condition 3", "yes", activity("action3"))
                                .orElse("none", activity("action4")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                if (big?) then (yes)
                :action;
                (no) elseif (condition 1) then (yes)
                :action1;
                :action3;
                (no) elseif (condition 2) then (yes)
                :action2;
                (no) elseif (condition 3) then (yes)
                :action3;
                else (none)
                :action4;
                endif
                @enduml""");
    }
}
