package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.ActionBuilder.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.ParallelProcess.andDoInParallel;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ParallelProcessingFlowchartGeneratorTest {
    // TODO: P2 styling on fork
    // TODO: P2 config

    @Nested
    class Simple {

        @Test
        void multipleParallelActivities() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(first(activity("action1")))
                            .and(following(activity("action2")).then(activity("action3")))
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
        void multipleParallelActivitiesLabelOnJoinNode() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(first(activity("action1")))
                            .and(following(activity("action2")).then(activity("action3")))
                            .and(activity("action4"))
                            .joinLabel("and"))
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
                    end fork (and)
                    @enduml""");
        }

        @Test
        void multipleParallelActivitiesAndMerge() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(first(activity("action1")))
                            .and(following(activity("action2")).then(activity("action3")))
                            .and(activity("action4")).merge())
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
                    end merge
                    @enduml""");
        }

        @Test
        void multipleParallelActivitiesWithLabelsFromFork() {
            String flowchart = flowchart()
                    .then(doInParallel()
                            .the(label("label 1"), first(activity("action1")))
                            .an(label("label 2"),following(activity("action2")).then(activity("action3")))
                            .and(label("label 3"), activity("action4")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    fork
                    ->label 1;
                    :action1;
                    fork again
                    ->label 2;
                    :action2;
                    :action3;
                    fork again
                    ->label 3;
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
