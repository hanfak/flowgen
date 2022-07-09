package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Nodes.END;
import static com.hanfak.flowgen.Nodes.STOP;
import static com.hanfak.flowgen.Split.split;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class SplitProcessingFlowchartGeneratorTest {
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
                            .andDo(activity("action2b"), STOP)
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
                    stop
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
    class MultiStartSplit {

        @Test
        void haveMultipleStartingActions() {
            String flowchart = flowchart()
                    .then(split()
                            .andDoStart(activity("action2a"))
                            .andDoStart(activity("action2b"))
                            .andDoStart(activity("action2c")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    split
                    -[hidden]->
                    :action2a;
                    split again
                    -[hidden]->
                    :action2b;
                    split again
                    -[hidden]->
                    :action2c;
                    end split
                    :action3;
                    @enduml""");
        }

        @Test
        void haveMultipleStartingActionsAndSingleActionLeadingToSplit() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDoStart(activity("action2a"))
                            .andDoStart(activity("action2b"))
                            .andDoStart(activity("action2c"))
                            .andDo(activity("action4"))
                            .andDo(activity("action5")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    split
                    -[hidden]->
                    :action2a;
                    split again
                    -[hidden]->
                    :action2b;
                    split again
                    -[hidden]->
                    :action2c;
                    split again
                    :action4;
                    split again
                    :action5;
                    end split
                    :action3;
                    @enduml""");
        }
    }

    @Nested
    class MultiEndSplit {
        @Test
        void multiEnd() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDo(activity("action2a"), andExit())
                            .andDo(activity("action2b"), andExit())
                            .andDo(activity("action2c"), andExit()))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    :action1;
                    split
                    :action2a;
                    detach
                    split again
                    :action2b;
                    detach
                    split again
                    :action2c;
                    detach
                    end split
                    @enduml""");
        }

        @Test
        void multiEndAndSplitWhichCarryOn() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDo(activity("action2a"), andExit())
                            .andDo(activity("action2b"), STOP)
                            .andDo(activity("action2c"), END)
                            .andDo(activity("action2d"))
                            .andDo(activity("action2e")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    :action1;
                    split
                    :action2a;
                    detach
                    split again
                    :action2b;
                    stop
                    split again
                    :action2c;
                    end
                    split again
                    :action2d;
                    split again
                    :action2e;
                    end split
                    :action3;
                    @enduml""");
        }
    }

    @Nested
    class Labels {
        @Test
        void labelsAtStartOfSplitActions() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .then(split()
                            .andDoWith(label("do"), activity("action2a"))
                            .andDoWith(label("do"), activity("action2b"), label("next"), activity("action2bb"))
                            .andDoWith(label("do"), activity("action2c").label("hello")))
                    .then(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    :action1;
                    split
                    ->do;
                    :action2a;
                    split again
                    ->do;
                    :action2b;
                    ->next;
                    :action2bb;
                    split again
                    ->do;
                    :action2c;
                    ->hello;
                    end split
                    :action3;
                    @enduml""");
        }
    }
}
