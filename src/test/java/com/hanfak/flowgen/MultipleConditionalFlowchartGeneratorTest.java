package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.ElseIfBuilder.elseIf;
import static com.hanfak.flowgen.MultiConditional.ifTrueFor;
import static com.hanfak.flowgen.ThenBuilder.forValue;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class MultipleConditionalFlowchartGeneratorTest {
    // TODO: P2 arrow style after if, after elseif, after else, after endif
    // TODO: P2 inline styling
    @Test
    void multipleIfELseWithPredicateLabelsOnAllPaths() {
        String flowchart = flowchart()
                .then(ifTrueFor("big?")
                                .then(forValue("yes").and(doActivity("action")))
                                .then(elseIf("condition 1?")
                                        .thenDo(an(activity("action1")).and(activity("action3")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .then(elseIf("condition 2?")
                                        .then(an(activity("action2")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .then(elseIf("condition 3?")
                                        .then(an(activity("action3")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .orElse(elseDo(doActivity("action4")).and(doActivity("action4")).forValue("none")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action;\n" +
                                                           "(no) elseif (condition 1?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "(no) elseif (condition 2?) then (yes)\n" +
                                                           ":action2;\n" +
                                                           "(no) elseif (condition 3?) then (yes)\n" +
                                                           ":action3;\n" +
                                                           "else (none)\n" +
                                                           ":action4;\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void multipleIfELseWithPredicatesOnAllPathsApartFromElseIf() {
        String flowchart = flowchart()
                .then(ifTrueFor("big?")
                        .then("yes", doActivity("action"))
                        .then(elseIf("condition 1?")
                                .then(an(activity("action1")).and(activity("action3")))
                                .elseIfLabel("yes"))
                        .orElse(elseDo(doActivity("action4")).forValue("none")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action;\n" +
                                                           "elseif (condition 1?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (none)\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void multipleIfELseWithPredicateLabelsOnAllPathsApartFromLastElse() {
        String flowchart = flowchart()
                .then(ifTrueFor("big?")
                        .then("yes", doActivity("action"))
                        .then(elseIf("condition 1?")
                                .then(an(activity("action1")).and(activity("action3")))
                                .elseLabel("no").elseIfLabel("yes"))
                        .orElse(elseDo(doActivity("action4"))))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action;\n" +
                                                           "(no) elseif (condition 1?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void multipleIfELseWithPredicateLabelsOnAllPathsApartFromElseIfThen() {
        String flowchart = flowchart()
                .then(ifTrueFor("big?")
                        .then(forValue("yes").then(doActivity("action")))
                        .then(elseIf("condition 1?")
                                .then(an(activity("action1")).and(activity("action3")))
                                .elseLabel("no"))
                        .orElse(elseDo(doActivity("action4")).forValue("none")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action;\n" +
                                                           "(no) elseif (condition 1?) then\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (none)\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void multipleIfELseWithPredicateLabelsOnAllPathsApartFromElseIfThenAndElseIfElse() {
        String flowchart = flowchart()
                .then(ifTrueFor("big?")
                        .then(forValue("yes").then(doActivity("action")))
                        .then(elseIf("condition 1?")
                                .then(an(activity("action1")).and(activity("action3"))))
                        .orElse(elseDo(doActivity("action4")).forValue("none")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action;\n" +
                                                           "elseif (condition 1?) then\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (none)\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }
}
