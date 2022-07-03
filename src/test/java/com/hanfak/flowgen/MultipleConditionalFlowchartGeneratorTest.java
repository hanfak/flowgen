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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action;
                (no) elseif (condition 1?) then (yes)
                :action1;
                :action3;
                (no) elseif (condition 2?) then (yes)
                :action2;
                (no) elseif (condition 3?) then (yes)
                :action3;
                else (none)
                :action4;
                :action4;
                endif
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action;
                elseif (condition 1?) then (yes)
                :action1;
                :action3;
                else (none)
                :action4;
                endif
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action;
                (no) elseif (condition 1?) then (yes)
                :action1;
                :action3;
                else
                :action4;
                endif
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action;
                (no) elseif (condition 1?) then
                :action1;
                :action3;
                else (none)
                :action4;
                endif
                @enduml""");
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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                if (big?) then (yes)
                :action;
                elseif (condition 1?) then
                :action1;
                :action3;
                else (none)
                :action4;
                endif
                @enduml""");
    }
}
