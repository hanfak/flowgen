package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Group.group;
import static org.assertj.core.api.Assertions.assertThat;

class GroupFlowchartGeneratorTest {

    // TODO: P2 styling - diamond, line, colour

    @Test
    void simpleGroup() {
        String flowchart = flowchart()
                .has(group().with(doActivity("action1")).with(doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                group
                :action1;
                :action2;
                end group
                @enduml""");
    }

    @Test
    void simpleGroupMultipleActions() {
        String flowchart = flowchart()
                .has(group().containing(
                        doActivity("action1"),
                        doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                group
                :action1;
                :action2;
                end group
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                group
                :action1;
                :action2;
                end group
                group
                :action3;
                end group
                @enduml""");
    }

    @Test
    void simpleGroupWithName() {
        String flowchart = flowchart()
                .then(group("Name").with(doActivity("action1")).and(doActivity("action2")).last(doActivity("action3")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                group Name
                :action1;
                :action2;
                :action3;
                end group
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                group Name\nNew lines
                :action1;
                :action2;
                end group
                group
                :action3;
                group Name With Spaces
                :action1;
                :action2;
                end group
                end group
                @enduml""");
    }
}
