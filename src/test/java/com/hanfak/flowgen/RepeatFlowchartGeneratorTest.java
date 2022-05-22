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
                            .actions(activity("action1"), andActivity("action2"), thenActivity("action4"))
                            .and(activity("action3"))
                            .and(activity("action3a"), andActivity("action3b"))
                            .then(activity("action5"))
                            .then(activity("action6"), andActivity("action7"))
                            .repeatWhen("is Big?"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .action(activity("action1"))
                            .and(activity("action2"), andActivity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .then(activity("action3"))
                            .repeatWhen("is Big?")
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?")
                            .labelRepeat(activity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .labelRepeat(activity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?")
                            .exitOn("no")
                            .labelRepeat(activity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?")
                            .isTrueFor("yes")
                            .labelRepeat(activity("Repeat"))
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
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
