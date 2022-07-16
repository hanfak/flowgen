package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Group.group;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class GroupFlowchartGeneratorTest {

    // TODO: P2 styling - diamond, line, colour

    @Test
    void simpleGroup() {
        String flowchart = flowchart()
                .has(group().with(doActivity("action1")).with(doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "group\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end group\n" +
                                                           "@enduml");
    }

    @Test
    void simpleGroupMultipleActions() {
        String flowchart = flowchart()
                .has(group().containing(
                        doActivity("action1"),
                        doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "group\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end group\n" +
                                                           "@enduml");
    }

    @Test
    void simpleGroupFromFlowchart() {
        String flowchart = flowchart()
                .hasGroupWith(
                        activity("action1"),
                        thenActivity("action2"))
                .hasGroupWith(
                        activity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "group\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end group\n" +
                                                           "group\n" +
                                                           ":action3;\n" +
                                                           "end group\n" +
                                                           "@enduml");
    }

    @Test
    void simpleGroupWithName() {
        String flowchart = flowchart()
                .then(group("Name").with(doActivity("action1")).and(doActivity("action2")).last(doActivity("action3")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "group Name\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           ":action3;\n" +
                                                           "end group\n" +
                                                           "@enduml");
    }

    @Test
    void simpleGroupFromFlowchartWithName() {
        String flowchart = flowchart()
                .hasGroupWith("Name\nNew lines",
                        activity("action1"),
                        doActivity("action2"))
                .hasGroupWith(
                        activity("action3"),
                        group("Name With Spaces")
                                .with(doActivity("action1"))
                                .with(doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "group Name\nNew lines\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end group\n" +
                                                           "group\n" +
                                                           ":action3;\n" +
                                                           "group Name With Spaces\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end group\n" +
                                                           "end group\n" +
                                                           "@enduml");
    }
}
