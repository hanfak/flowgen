package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Split.split;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class SplitProcessingFlowchartGeneratorTest {
    // TODO: P1 add end/stop node in one split
    // TODO: P1 Labels on each split, use -> balh; (issues of overlap)
    // TODO: P1 Multi line Labels on each split, use -> balh; (issues of overlap)
    // TODO: P2 arrow style after split or split again, after end split
    // TODO: P2 styling on line label
    // TODO: P2 Config, styling

    @Nested
    class SimpleSplit {

        @Test
        void simpleSplitProcessing() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDo(activity("action2a"))
                            .andDo(activity("action2b"))
                            .andDo(activity("action2c")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    :action1;
                    split
                    :action2a;
                    split again
                    :action2b;
                    split again
                    :action2c;
                    end split
                    :action3;
                    @enduml""");
        }

        @Test
        void splitWhereActionCanTakeMultipleActions() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDo(an(activity("action2aa")).and(activity("action2ab")))
                            .andDo(activity("action2b"))
                            .andDo(activity("action2ca"), activity("action2cb"), activity("action2cc")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    :action1;
                    split
                    :action2aa;
                    :action2ab;
                    split again
                    :action2b;
                    split again
                    :action2ca;
                    :action2cb;
                    :action2cc;
                    end split
                    :action3;
                    @enduml""");

        }
    }

    @Nested
    class MultiStartSplit {}

    @Nested
    class MultiEndSplit {}
}
