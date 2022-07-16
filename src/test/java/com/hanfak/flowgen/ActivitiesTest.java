package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ActivitiesTest {

    @Nested
    class SynchronousActivityFlow {
        // TODO: P2 styling an individual activity
        @Test
        void createOneActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action;\n" +
                                                               "@enduml");
        }

        @Test
        void doActivities() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .then(activity("action"))
                    .then(andActivity("action"))
                    .then(and("action"))
                    .and(thenActivity("action"))
                    .and(then("action"))
                    .then(withActivity("action"))
                    .then(with("action"))
                    .then(executeActivity("action"))
                    .then(execute("action"))
                    .then(performActivity("action"))
                    .then(perform("action"))
                    .then(completeActivity("action"))
                    .then(complete("action"))
                    .then(finishActivity("action"))
                    .then(finish("action"))
                    .then(implementActivity("action"))
                    .then(implement("action"))
                    .then(prepareActivity("action"))
                    .then(prepare("action"))
                    .then(makeActivity("action"))
                    .then(make("action"))
                    .then(fixActivity("action"))
                    .then(fix("action"))
                    .then(produceActivity("action"))
                    .then(produce("action"))
                    .then(arrangeActivity("action"))
                    .then(arrange("action"))
                    .then(createActivity("action"))
                    .then(create("action"))
                    .then(designActivity("action"))
                    .then(design("action"))
                    .then(workOutActivity("action"))
                    .then(workOut("action"))
                    .then(calculateActivity("action"))
                    .then(calculate("action"))
                    .then(solveActivity("action"))
                    .then(solve("action"))
                    .then(resolveActivity("action"))
                    .then(resolve("action"))
                    .then(sendActivity("action"))
                    .then(send("action"))
                    .then(listenActivity("action"))
                    .then(listen("action"))
                    .then(listenForActivity("action"))
                    .then(listenFor("action"))
                    .then(writeActivity("action"))
                    .then(write("action"))
                    .then(updateActivity("action"))
                    .then(update("action"))
                    .then(deleteActivity("action"))
                    .then(delete("action"))
                    .then(readActivity("action"))
                    .then(read("action"))
                    .then(fetchActivity("action"))
                    .then(fetch("action"))
                    .then(retrieveActivity("action"))
                    .then(retrieve("action"))
                    .then(useActivity("action"))
                    .then(use("action"))
                    .then(startActivity("action"))
                    .then(start("action"))
                    .create();

            long numberOfActionsCreated = Arrays.stream(flowchart.split("\n"))
                    .filter(line -> line.contains(":action;"))
                    .count();
            assertThat(numberOfActionsCreated).isEqualTo(62L);
        }

        @Test
        void oneFlowBetweenMultipleActivities() {
            String flowchart = flowchart()
                    .start(activity("action1"))
                    .and(doActivity("action2"))
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesInOneStep() {
            String flowchart = flowchart()
                    .start(an(activity("action1"))
                            .then(activity("action2"))
                            .and(activity("action3")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }
    }

    @Nested
    class SwimLanes {
        @Test
        void createMultipleActivitiesInSwimLanes() {
            String flowchart = flowchart()
                    .then(activity("action").inSwimLane("S1"))
                    .then(doActivity("action 1").inSwimLane("S2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "|S1|\n" +
                                                               "|S1|\n" +
                                                               ":action;\n" +
                                                               "|S2|\n" +
                                                               ":action 1;\n" +
                                                               "@enduml");
        }
    }

    @Nested
    class Notes {

        // TODO: P2 styling a note
        @Nested
        class Simple {

            @Test
            void createOneActivityWithSimpleDefaultNote() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleNoteOnTheLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note left\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleNoteOnTheRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleDefaultFloatingNote() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note floating right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleFloatingNoteOnTheLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()
                                .left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note floating left\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleFloatingNoteOnTheRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()
                                .right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note floating right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createOneActivityWithSimpleNoteWithMultipleLocationUseLastOneLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").right().left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note left\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");

            }
            @Test
            void createOneActivityWithSimpleNoteWithMultipleLocationUseLastOneRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").left().right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");

            }

            @Nested
            class Complex {

                @Test
                void createOneActivityWithDefaultMultiLineNote() {
                    String flowchart = flowchart()
                            .then(doActivity("action").with(note("A Note\n" +
                                                                 "====\n" +
                                                                 "new //italic//\n" +
                                                                 "quoted \"\"foo()\"\"\n" +
                                                                 "* List 1\n" +
                                                                 "* List 2\n" +
                                                                 "====\n" +
                                                                 "contain <b>HTML</b>")))
                            .create();
                    assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                       ":action;\n" +
                                                                       "note right\n" +
                                                                       "A Note\n" +
                                                                       "====\n" +
                                                                       "new //italic//\n" +
                                                                       "quoted \"\"foo()\"\"\n" +
                                                                       "* List 1\n" +
                                                                       "* List 2\n" +
                                                                       "====\n" +
                                                                       "contain <b>HTML</b>\n" +
                                                                       "end note\n" +
                                                                       "@enduml");
                }
            }
        }

        @Nested
        class WithSwimLane {
            @Test
            void createMultipleActivitiesWithSimpleDefaultNoteInSwimLanes() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")).inSwimLane("S1"))
                        .then(doActivity("action 1").with(note("A Note 1")).inSwimLane("S2"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "|S1|\n" +
                                                                   "|S1|\n" +
                                                                   ":action;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "|S2|\n" +
                                                                   ":action 1;\n" +
                                                                   "note right\n" +
                                                                   "A Note 1\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }

            @Test
            void createMultipleActivitiesWithSimpleDefaultNoteAndLabelsInSwimLanes() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")).label("label 1").inSwimLane("S1"))
                        .then(doActivity("action 1").with(note("A Note 1")).label("label 2").inSwimLane("S2"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "|S1|\n" +
                                                                   "|S1|\n" +
                                                                   ":action;\n" +
                                                                   "->label 1;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "|S2|\n" +
                                                                   ":action 1;\n" +
                                                                   "->label 2;\n" +
                                                                   "note right\n" +
                                                                   "A Note 1\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }
        }

        @Nested
        class WithLabel {
            @Test
            void createOneActivityWithSimpleDefaultNoteAndLabel() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")).label("label"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   ":action;\n" +
                                                                   "->label;\n" +
                                                                   "note right\n" +
                                                                   "A Note\n" +
                                                                   "end note\n" +
                                                                   "@enduml");
            }
        }
    }

    @Nested
    class WithLabel {
        @Test
        void createOneActivityWithLabel() {
            String flowchart = flowchart()
                    .then(doActivity("action").label("label"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action;\n" +
                                                               "->label;\n" +
                                                               "@enduml");
        }

        @Nested
        class SwimLanes {
            @Test
            void createMultipleActivitiesWithLabelsInSwimLanes() {
                String flowchart = flowchart()
                        .then(andActivity("action").label("label 1").inSwimLane("S1"))
                        .then(then("action 1").label("label 2").inSwimLane("S2"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                                   "|S1|\n" +
                                                                   "|S1|\n" +
                                                                   ":action;\n" +
                                                                   "->label 1;\n" +
                                                                   "|S2|\n" +
                                                                   ":action 1;\n" +
                                                                   "->label 2;\n" +
                                                                   "@enduml");
            }
        }
    }

    @Nested
    class ActivitySignals {
        @Test
        void sendActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").sendStyle())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2>\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }

        @Test
        void receiveActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").receiveStyle())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2<\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }

        @Test
        void squareActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").square())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2]\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }

        @Test
        void angledActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").angled())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               ":action2>\n" +
                                                               ":action3;\n" +
                                                               "@enduml");
        }
    }
}
