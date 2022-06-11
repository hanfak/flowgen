package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Activity.andActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.multiIf;
import static org.assertj.core.api.Assertions.assertThat;

// TODO: combine with conditional
class MultipleConditionalFlowchartGeneratorTest {
    // TODO: better naming and implementation
    // TODO: No predicate outcome show for then branch
    // TODO: No predicate outcome show for multiple then branch
    // TODO: No predicate outcome show for multiple elseif branch
    // TODO: No predicate outcome show for orelse branch
    // TODO: guard clause
    // TODO: arrow style after if, after elseif, after else, after endif
    // TODO: inline styling
    @Test
    void multipleIfELseWithPredicatesOnAllPaths() {
        String flowchart = flowchart()
                .then(
                        multiIf("big?")
                                .then("yes", doActivity("action"))
                                .elseIf("no", "condition 1?", "yes", doActivity("action1"), andActivity("action3"))
                                .elseIf("no", "condition 2?", "yes", doActivity("action2"))
                                .elseIf("no", "condition 3?", "yes", doActivity("action3"))
                                .orElse("none", doActivity("action4")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                if (big?) then (yes)
                :action;
                (no) elseif (condition 1?) then (yes)
                :action1;
                :action3;
                (no) elseif (condition 2?) then (yes)
                :action2;
                (no) elseif (condition 3?) then (yes)
                :action3;
                else (none)
                :action4;
                endif
                @enduml""");
    }
}
