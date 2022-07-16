package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.ActionBuilder.doAn;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Conditional.*;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.ThenBuilder.forValue;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ConditionalFlowChartGeneratorTest {

    // TODO: P2 arrow style after than, after endif
    // TODO: P2 styling - diamond, line, colour
    @Test
    void ifELseWithPredicatesOnBothPaths() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), andActivity("action3"))
                        .orElseFor("no", doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           ":action2;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void ifELseWithNoPredicatesDefinedOnBothPaths() {
        String flowchart = flowchart()
                .then(ifThe("house is big?")
                        .then(doActivity("action1"))
                        .or(elseDo(activity("action2"))))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (house is big?) then\n" +
                                                           ":action1;\n" +
                                                           "else\n" +
                                                           ":action2;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void ifELseDetachesOnIfBranch() {
        String flowchart = flowchart()
                .then(branchWhen("is big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3"))
                                .and(exit()))
                        .or(elseDo(activity("action2"))
                                .and(doActivity("action5"))
                                .forValue("no")))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "detach\n" +
                                                           "else (no)\n" +
                                                           ":action2;\n" +
                                                           ":action5;\n" +
                                                           "endif\n" +
                                                           ":action4;\n" +
                                                           "@enduml");
    }

    @Test
    void ifELseDetachesOnElseBranch() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doAn(activity("action1")).and(activity("action3")))
                        .orElseFor("no", doActivity("action2"), andExit()))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           ":action2;\n" +
                                                           "detach\n" +
                                                           "endif\n" +
                                                           ":action4;\n" +
                                                           "@enduml");
    }

    @Test
    void elsePathHasNoPredicateDefined() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), thenActivity("action3"), and("action4"))
                        .orElseFor(doActivity("action2")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           ":action4;\n" +
                                                           "else\n" +
                                                           ":action2;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void ifWithoutElse() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "endif\n" +
                                                           "@enduml");
    }

    @Test
    void ifWithoutElseLabelExitConnector() {
        String flowchart = flowchart()
                .then(ifIt("is big?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .existLabel("NOK"))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "endif\n" +
                                                           "->NOK;\n" +
                                                           ":action4;\n" +
                                                           "@enduml");
    }

    @Test
    void ifElseLabelExitConnector() {
        String flowchart = flowchart()
                .then(ifWhen("200?")
                        .thenFor("yes", doActivity("action1"), doActivity("action3"))
                        .orElseFor("no", doAn(activity("action2")).and(activity("action5")))
                        .existLabel("NOK"))
                .then(doActivity("action4"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (200?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           ":action2;\n" +
                                                           ":action5;\n" +
                                                           "endif\n" +
                                                           "->NOK;\n" +
                                                           ":action4;\n" +
                                                           "@enduml");
    }

    @Test
    void nestedIf() {
        String flowchart = flowchart()
                .then(ifIsTrue("is big?")
                        .then(forValue("yes").then(doActivity("action1")))
                        .orElseFor("no",
                                ifIs("tiny?")
                                        .thenFor("yes", ifIs("tiny?")
                                                .thenFor("yes", doActivity("action2"))
                                                .orElseFor("no", doActivity("action3"))
                                                .existLabel("Next 0"))
                                        .orElseFor("no", doActivity("action3"))
                                        .existLabel("Next 1"),
                                doActivity("action4"))
                        .existLabel("Next 2"))
                .then(doActivity("action5"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (is big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           "else (no)\n" +
                                                           "if (tiny?) then (yes)\n" +
                                                           "if (tiny?) then (yes)\n" +
                                                           ":action2;\n" +
                                                           "else (no)\n" +
                                                           ":action3;\n" +
                                                           "endif\n" +
                                                           "->Next 0;\n" +
                                                           "else (no)\n" +
                                                           ":action3;\n" +
                                                           "endif\n" +
                                                           "->Next 1;\n" +
                                                           ":action4;\n" +
                                                           "endif\n" +
                                                           "->Next 2;\n" +
                                                           ":action5;\n" +
                                                           "@enduml");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithLabelElseBranch() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           "endif\n" +
                                                           ":action2;\n" +
                                                           "@enduml");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithLabelElseBranchAndExitLabel() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no")
                        .existLabel("next"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           "endif\n" +
                                                           "->next;\n" +
                                                           ":action2;\n" +
                                                           "@enduml");
    }

    @Test
    void ifWithGuardClauseNoReturnEarlyWithNoThenLabelAndLabelElseBranchAndExitLabel() {
        String flowchart = flowchart()
                .then(ifIs("big?")
                        .then(doAn(doActivity("action1"))
                                .and(activity("action3")))
                        .elseLabel("no")
                        .existLabel("next"))
                .then(doActivity("action2")).create();

        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "if (big?) then\n" +
                                                           ":action1;\n" +
                                                           ":action3;\n" +
                                                           "else (no)\n" +
                                                           "endif\n" +
                                                           "->next;\n" +
                                                           ":action2;\n" +
                                                           "@enduml");
    }
}
