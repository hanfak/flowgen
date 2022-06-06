package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

class SwimLanesFlowchartGeneratorTest {

    // TODO: styling - diamond, line, colour

    @Test
    void oneSwimLane() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                |Swim Lane|
                |Swim Lane|
                :action1;
                @enduml""");
    }

    @Test
    void swimLaneWhenNotDefinedAtStart() {
        String flowchart = flowchart()
                .withTitle("Hello")
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                |Swim Lane|
                title
                Hello
                end title
                |Swim Lane|
                :action1;
                @enduml""");
    }

    @Test
    void multipleSameSwimLaneMultipleActivities() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .then(doActivity("action2").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                |Swim Lane|
                |Swim Lane|
                :action1;
                |Swim Lane|
                :action2;
                @enduml""");
    }

    @Test
    void multipleSwimLaneMultipleActivities() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .then(doActivity("action2").inSwimLane("Swim Lane 1"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                |Swim Lane|
                |Swim Lane|
                :action1;
                |Swim Lane 1|
                :action2;
                @enduml""");
    }

    @Test
    void multipleSwimLaneMultipleActivitiesCarryOver() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane 1"))
                .then(doActivity("action2").inSwimLane("Swim Lane 2"))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml 
                |Swim Lane 1|
                |Swim Lane 1|
                :action1;
                |Swim Lane 2|
                :action2;
                :action3;
                @enduml""");
    }
}
