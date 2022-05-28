package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;
import static org.assertj.core.api.Assertions.assertThat;

class RepeatFlowchartGeneratorTest {

    // TODO: styling - diamond, line, colour
    // TODO: Label at start of repeat action
    // TODO: break in repeat, merge with before next action outside of repeat
    // TODO: use not (%s) instead of arrow for exit label, rename to not
    // TODO: step builder to force correct usage
    @Nested
    class Simple {

        @Test
        void simpleRepeatWithoutLabels() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), andActivity("action2"), thenActivity("action4"))
                            .and(doActivity("action3"))
                            .and(doActivity("action3a"), andActivity("action3b"))
                            .then(doActivity("action5"))
                            .then(doActivity("action6"), andActivity("action7"))
                            .repeatWhen("is Big?"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    :action3a;
                    :action3b;
                    :action5;
                    :action6;
                    :action7;
                    repeat while (is Big?)
                    @enduml""");
        }

        @Test
        void simpleTrueLabelPredicateIs() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doAction(activity("action1"))
                            .and(doActivity("action2"), andActivity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    @enduml""");
        }

        @Test
        void simpleFalseLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .then(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?)
                    ->no;
                    @enduml""");
        }

        @Test
        void simpleWithBothLabelsPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .doActions(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
        }
    }

    @Nested
    class RepeatLabel {

        @Test
        void withoutLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .doActions(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .labelRepeat(doActivity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:This is repeated;
                    repeat while (is Big?)
                    @enduml""");
        }

        @Test
        void withTrueLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .doActions(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .labelRepeat(doActivity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:This is repeated;
                    repeat while (is Big?) is (yes)
                    @enduml""");
        }

        @Test
        void withExitLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .doActions(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .exitOn("no")
                            .labelRepeat(doActivity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:This is repeated;
                    repeat while (is Big?)
                    ->no;
                    @enduml""");
        }

        @Test
        void withTrueAndExitLabels() {
            String flowchart = flowchart()
                    .then(repeat()
                            .doActions(doActivity("action1"), doActivity("action2"))
                            .doActions(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .labelRepeat(doActivity("Repeat"))
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml 
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:Repeat;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
        }
    }
}
