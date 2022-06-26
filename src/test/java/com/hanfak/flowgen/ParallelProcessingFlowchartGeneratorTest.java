package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.ActionBuilder.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.ParallelProcess.andDoInParallel;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ParallelProcessingFlowchartGeneratorTest {
    // TODO: P1 merge 2 and more than 2 actions
    // TODO: P1 end one parallel action, use node ,detach, kill
    // TODO: P1 arrow style after start of fork or fork again,
    // TODO: P1 label on end fork
    // TODO: P2 styling on fork
    // TODO: P2 config

    @Nested
    class Simple {

        @Test
        void multipleParallelActivities() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(first(activity("action1")))
                            .and(following(activity("action2")).and(activity("action3")))
                            .and(activity("action4")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    fork
                    :action1;
                    fork again
                    :action2;
                    :action3;
                    fork again
                    :action4;
                    end fork
                    @enduml""");
        }

        @Test
        void nestedParallelActivities() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .an(activity("action1"))
                            .an(andDoInParallel().the(activity("action2")).and(activity("action3"))))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    fork
                    :action1;
                    fork again
                    fork
                    :action2;
                    fork again
                    :action3;
                    end fork
                    end fork
                    @enduml""");
        }
    }
}
