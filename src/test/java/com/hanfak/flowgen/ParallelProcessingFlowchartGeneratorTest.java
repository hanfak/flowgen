package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.thenActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static org.assertj.core.api.Assertions.assertThat;

class ParallelProcessingFlowchartGeneratorTest {
    // TODO: merge 2 and more than 2 actions
    // TODO: end one parallel action, use node ,detach, kill
    // TODO: arrow style after start of fork or fork again,
    // TODO: label on end fork
    // TODO: styling on fork
    // TODO: config

    @Nested
    class Simple {

        @Test
        void multipleParallelActivities() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(activity("action1"))
                            .the(activity("action2"), thenActivity("action3"))
                            .the(activity("action4")))
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
    }
}
