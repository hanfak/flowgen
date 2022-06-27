package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class RepeatFlowchartGeneratorTest {

    // TODO: P2 arrow style on repeat section, on branch after action in repeat section, after repeat finishes, at start of repeat,
    // TODO: P2 step builder to force correct usage
    // TODO: P2 styling - diamond, line, colour
    // TODO: PX use  continue (see git history for expected example) may not be possible (will need to wait for update in plantuml) may have to use goto

    @Nested
    class Simple {

        @Test
        void simpleRepeatWithoutLabels() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"), andActivity("action2"), thenActivity("action4"))
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
                    :action4;
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
        void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInSeparateMethods() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"))
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
        void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInOneMethod() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"))
                            .and(doActivity("action2"), andActivity("action3"))
                            .repeatWhen("is Big?", "yes"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .then(doActivity("action3"))
                            .repeatWhen("is Big?").exitOn("no"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
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

        @Test
        void simpleWithBothLabelsPredicateInOneMethod() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?", "yes", "no"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
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
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
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

    @Nested
    class BreakInside {
        @Test
        void simpleRepeatWithoutArrowLabelsWithIf() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"))
                            .and(ifIsTrue("is big?")
                                    .thenFor("yes", doActivity("action55"), doActivity("action66"), leave()))
                            .the(activity("action2"))
                            .repeatWhen("is Big?"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat
                    :action1;
                    if (is big?) then (yes)
                    :action55;
                    :action66;
                    break
                    endif
                    :action2;
                    repeat while (is Big?)
                    @enduml""");
        }
    }

    @Nested
    class FirstActionAtStart {
        @Nested
        class Simple {

            @Test
            void simpleRepeatWithoutLabels() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"), andActivity("action2"), thenActivity("action4"))
                                .and(doActivity("action3"))
                                .and(doActivity("action3a"), andActivity("action3b"))
                                .then(doActivity("action5"))
                                .then(doActivity("action6"), andActivity("action7"))
                                .repeatWhen("is Big?"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    :action2;
                    :action4;
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
            void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInSeparateMethods() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"))
                                .and(doActivity("action2"), andActivity("action3"))
                                .repeatWhen("is Big?")
                                .isTrueFor("yes"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    @enduml""");
            }

            @Test
            void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInOneMethod() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"))
                                .and(doActivity("action2"), andActivity("action3"))
                                .repeatWhen("is Big?", "yes"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    @enduml""");
            }

            @Test
            void simpleFalseLabelPredicate() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .then(doActivity("action3"))
                                .repeatWhen("is Big?").exitOn("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
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
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .isTrueFor("yes")
                                .exitOn("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
            }

            @Test
            void simpleWithBothLabelsPredicateInOneMethod() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?", "yes", "no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
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
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .labelRepeat(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
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
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .isTrueFor("yes")
                                .labelRepeat(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
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
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .exitOn("no")
                                .labelRepeat(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
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
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .isTrueFor("yes")
                                .labelRepeat(doActivity("Repeat"))
                                .exitOn("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    :action2;
                    :action3;
                    backward:Repeat;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
            }
        }

        @Nested
        class BreakInside {
            @Test
            void simpleRepeatWithoutArrowLabelsWithIf() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"))
                                .and(ifIsTrue("is big?")
                                        .thenFor("yes", doActivity("action55"), doActivity("action66"), leave()))
                                .the(activity("action2"))
                                .repeatWhen("is Big?"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    repeat :First Action;
                    :action1;
                    if (is big?) then (yes)
                    :action55;
                    :action66;
                    break
                    endif
                    :action2;
                    repeat while (is Big?)
                    @enduml""");
            }
        }
    }
}
