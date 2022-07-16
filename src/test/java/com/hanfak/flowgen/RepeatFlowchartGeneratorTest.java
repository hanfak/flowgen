package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.ActionBuilder.next;
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action4;\n" +
                                                               ":action3;\n" +
                                                               ":action3a;\n" +
                                                               ":action3b;\n" +
                                                               ":action5;\n" +
                                                               ":action6;\n" +
                                                               ":action7;\n" +
                                                               "repeat while (is Big?)\n" +
                                                               "@enduml");
        }

        @Test
        void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInSeparateMethods() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"))
                            .and(doActivity("action2"), andActivity("action3"))
                            .repeatWhen("is Big?").is("yes"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "@enduml");
        }

        @Test
        void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInOneMethod() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(activity("action1"))
                            .and(doActivity("action2"), andActivity("action3"))
                            .repeatWhen("is Big?", "yes"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "@enduml");
        }

        @Test
        void simpleFalseLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .then(doActivity("action3"))
                            .repeatWhen("is Big?").leaveWhen("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "repeat while (is Big?)\n" +
                                                               "->no;\n" +
                                                               "@enduml");
        }

        @Test
        void simpleWithBothLabelsPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(next(activity("action1")).then(doActivity("action2")))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?").is("yes")
                            .leaveWhen("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "->no;\n" +
                                                               "@enduml");
        }

        @Test
        void simpleWithBothLabelsPredicateInOneMethod() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?", "yes", "no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "->no;\n" +
                                                               "@enduml");
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
                            .repeatLabel(doActivity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "backward:This is repeated;\n" +
                                                               "repeat while (is Big?)\n" +
                                                               "@enduml");
        }

        @Test
        void withTrueLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .is("yes")
                            .repeatLabel(doActivity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "backward:This is repeated;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "@enduml");
        }

        @Test
        void withExitLabelPredicate() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?")
                            .repeatLabel(withActivity("This is repeated"))
                            .leaveWhen("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "backward:This is repeated;\n" +
                                                               "repeat while (is Big?)\n" +
                                                               "->no;\n" +
                                                               "@enduml");
        }

        @Test
        void withTrueAndExitLabels() {
            String flowchart = flowchart()
                    .then(repeat()
                            .the(doActivity("action1"), thenActivity("action2"))
                            .and(doActivity("action3"))
                            .repeatWhen("is Big?").is("yes")
                            .repeatLabel(with("Repeat"))
                            .leaveWhen("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "backward:Repeat;\n" +
                                                               "repeat while (is Big?) is (yes)\n" +
                                                               "->no;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "repeat\n" +
                                                               ":action1;\n" +
                                                               "if (is big?) then (yes)\n" +
                                                               ":action55;\n" +
                                                               ":action66;\n" +
                                                               "break\n" +
                                                               "endif\n" +
                                                               ":action2;\n" +
                                                               "repeat while (is Big?)\n" +
                                                               "@enduml");
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
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action4;\n" +
                                                                   ":action3;\n" +
                                                                   ":action3a;\n" +
                                                                   ":action3b;\n" +
                                                                   ":action5;\n" +
                                                                   ":action6;\n" +
                                                                   ":action7;\n" +
                                                                   "repeat while (is Big?)\n" +
                                                                   "@enduml");
            }

            @Test
            void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInSeparateMethods() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"))
                                .and(doActivity("action2"), andActivity("action3"))
                                .repeatWhen("is Big?")
                                .is("yes"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "@enduml");
            }

            @Test
            void simpleRepeatWhenArrowLabelledWhenPredicateIsTrueInOneMethod() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(activity("action1"))
                                .and(doActivity("action2"), andActivity("action3"))
                                .repeatWhen("is Big?", "yes"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "@enduml");
            }

            @Test
            void simpleFalseLabelPredicate() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .then(doActivity("action3"))
                                .repeatWhen("is Big?").leaveWhen("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "repeat while (is Big?)\n" +
                                                                   "->no;\n" +
                                                                   "@enduml");
            }

            @Test
            void simpleWithBothLabelsPredicate() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .is("yes")
                                .leaveWhen("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "->no;\n" +
                                                                   "@enduml");
            }

            @Test
            void simpleWithBothLabelsPredicateInOneMethod() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?", "yes", "no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "->no;\n" +
                                                                   "@enduml");
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
                                .repeatLabel(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "backward:This is repeated;\n" +
                                                                   "repeat while (is Big?)\n" +
                                                                   "@enduml");
            }

            @Test
            void withTrueLabelPredicate() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .is("yes")
                                .repeatLabel(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "backward:This is repeated;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "@enduml");
            }

            @Test
            void withExitLabelPredicate() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .leaveWhen("no")
                                .repeatLabel(doActivity("This is repeated")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "backward:This is repeated;\n" +
                                                                   "repeat while (is Big?)\n" +
                                                                   "->no;\n" +
                                                                   "@enduml");
            }

            @Test
            void withTrueAndExitLabels() {
                String flowchart = flowchart()
                        .then(repeat(activity("First Action"))
                                .the(doActivity("action1"), thenActivity("action2"))
                                .and(doActivity("action3"))
                                .repeatWhen("is Big?")
                                .is("yes")
                                .repeatLabel(doActivity("Repeat"))
                                .leaveWhen("no"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   ":action2;\n" +
                                                                   ":action3;\n" +
                                                                   "backward:Repeat;\n" +
                                                                   "repeat while (is Big?) is (yes)\n" +
                                                                   "->no;\n" +
                                                                   "@enduml");
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
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "repeat :First Action;\n" +
                                                                   ":action1;\n" +
                                                                   "if (is big?) then (yes)\n" +
                                                                   ":action55;\n" +
                                                                   ":action66;\n" +
                                                                   "break\n" +
                                                                   "endif\n" +
                                                                   ":action2;\n" +
                                                                   "repeat while (is Big?)\n" +
                                                                   "@enduml");
            }
        }
    }
}
