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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               ":action2a;\n" +
                                                               "split again\n" +
                                                               ":action2b;\n" +
                                                               "split again\n" +
                                                               ":action2c;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               ":action2aa;\n" +
                                                               ":action2ab;\n" +
                                                               "split again\n" +
                                                               ":action2b;\n" +
                                                               "stop\n" +
                                                               "split again\n" +
                                                               ":action2ca;\n" +
                                                               ":action2cb;\n" +
                                                               ":action2cc;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");

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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "split\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2a;\n" +
                                                               "split again\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2b;\n" +
                                                               "split again\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2c;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2a;\n" +
                                                               "split again\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2b;\n" +
                                                               "split again\n" +
                                                               "-[hidden]->\n" +
                                                               ":action2c;\n" +
                                                               "split again\n" +
                                                               ":action4;\n" +
                                                               "split again\n" +
                                                               ":action5;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               ":action2a;\n" +
                                                               "detach\n" +
                                                               "split again\n" +
                                                               ":action2b;\n" +
                                                               "detach\n" +
                                                               "split again\n" +
                                                               ":action2c;\n" +
                                                               "detach\n" +
                                                               "end split\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               ":action2a;\n" +
                                                               "detach\n" +
                                                               "split again\n" +
                                                               ":action2b;\n" +
                                                               "stop\n" +
                                                               "split again\n" +
                                                               ":action2c;\n" +
                                                               "end\n" +
                                                               "split again\n" +
                                                               ":action2d;\n" +
                                                               "split again\n" +
                                                               ":action2e;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "split\n" +
                                                               "->do;\n" +
                                                               ":action2a;\n" +
                                                               "split again\n" +
                                                               "->do;\n" +
                                                               ":action2b;\n" +
                                                               "->next;\n" +
                                                               ":action2bb;\n" +
                                                               "split again\n" +
                                                               "->do;\n" +
                                                               ":action2c;\n" +
                                                               "->hello;\n" +
                                                               "end split\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }
    }
}
