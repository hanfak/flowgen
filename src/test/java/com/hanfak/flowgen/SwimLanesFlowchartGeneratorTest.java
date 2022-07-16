package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class SwimLanesFlowchartGeneratorTest {

    // TODO: P2 styling - diamond, line, colour

    @Test
    void oneSwimLane() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "|Swim Lane|\n" +
                                                           "|Swim Lane|\n" +
                                                           ":action1;\n" +
                                                           "@enduml");
    }

    @Test
    void swimLaneWhenNotDefinedAtStart() {
        String flowchart = flowchart()
                .withTitle("Hello")
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "|Swim Lane|\n" +
                                                           "title\n" +
                                                           "Hello\n" +
                                                           "end title\n" +
                                                           "|Swim Lane|\n" +
                                                           ":action1;\n" +
                                                           "@enduml");
    }

    @Test
    void multipleSameSwimLaneMultipleActivities() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .then(doActivity("action2").inSwimLane("Swim Lane"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "|Swim Lane|\n" +
                                                           "|Swim Lane|\n" +
                                                           ":action1;\n" +
                                                           "|Swim Lane|\n" +
                                                           ":action2;\n" +
                                                           "@enduml");
    }

    @Test
    void multipleSwimLaneMultipleActivities() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane"))
                .then(doActivity("action2").inSwimLane("Swim Lane 1"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "|Swim Lane|\n" +
                                                           "|Swim Lane|\n" +
                                                           ":action1;\n" +
                                                           "|Swim Lane 1|\n" +
                                                           ":action2;\n" +
                                                           "@enduml");
    }

    @Test
    void multipleSwimLaneMultipleActivitiesCarryOver() {
        String flowchart = flowchart()
                .then(doActivity("action1").inSwimLane("Swim Lane 1"))
                .then(doActivity("action2").inSwimLane("Swim Lane 2"))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "|Swim Lane 1|\n" +
                                                           "|Swim Lane 1|\n" +
                                                           ":action1;\n" +
                                                           "|Swim Lane 2|\n" +
                                                           ":action2;\n" +
                                                           ":action3;\n" +
                                                           "@enduml");
    }
}
