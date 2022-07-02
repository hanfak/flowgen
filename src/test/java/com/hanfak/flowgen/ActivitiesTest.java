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
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action;
                    @enduml""");
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
                    .create();

            long numberOfActionsCreated = Arrays.stream(flowchart.split("\n"))
                    .filter(line -> line.contains(":action;"))
                    .count();
            assertThat(numberOfActionsCreated).isEqualTo(60L);
        }

        @Test
        void oneFlowBetweenMultipleActivities() {
            String flowchart = flowchart()
                    .start(activity("action1"))
                    .and(doActivity("action2"))
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2;
                    :action3;
                    @enduml""");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesInOneStep() {
            String flowchart = flowchart()
                    .start(an(activity("action1"))
                            .then(activity("action2"))
                            .and(activity("action3")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2;
                    :action3;
                    @enduml""");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        |S1|
                        |S1|
                        :action;
                        |S2|
                        :action 1;
                        @enduml""");
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
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note right
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleNoteOnTheLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note left
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleNoteOnTheRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note right
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleDefaultFloatingNote() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note floating right
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleFloatingNoteOnTheLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()
                                .left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note floating left
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleFloatingNoteOnTheRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").floating()
                                .right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note floating right
                        A Note
                        end note
                        @enduml""");
            }

            @Test
            void createOneActivityWithSimpleNoteWithMultipleLocationUseLastOneLeft() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").right().left()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note left
                        A Note
                        end note
                        @enduml""");

            }
            @Test
            void createOneActivityWithSimpleNoteWithMultipleLocationUseLastOneRight() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note").left().right()))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        note right
                        A Note
                        end note
                        @enduml""");

            }

            @Nested
            class Complex {

                @Test
                void createOneActivityWithDefaultMultiLineNote() {
                    String flowchart = flowchart()
                            .then(doActivity("action").with(note("""
                                    A Note
                                    ====
                                    new //italic//
                                    quoted ""foo()""
                                    * List 1
                                    * List 2
                                    ====
                                    contain <b>HTML</b>""")))
                            .create();
                    assertThat(flowchart).isEqualToNormalizingNewlines("""
                            @startuml
                            :action;
                            note right
                            A Note
                            ====
                            new //italic//
                            quoted ""foo()""
                            * List 1
                            * List 2
                            ====
                            contain <b>HTML</b>
                            end note
                            @enduml""");
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
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        |S1|
                        |S1|
                        :action;
                        note right
                        A Note
                        end note
                        |S2|
                        :action 1;
                        note right
                        A Note 1
                        end note
                        @enduml""");
            }

            @Test
            void createMultipleActivitiesWithSimpleDefaultNoteAndLabelsInSwimLanes() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")).label("label 1").inSwimLane("S1"))
                        .then(doActivity("action 1").with(note("A Note 1")).label("label 2").inSwimLane("S2"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        |S1|
                        |S1|
                        :action;
                        ->label 1;
                        note right
                        A Note
                        end note
                        |S2|
                        :action 1;
                        ->label 2;
                        note right
                        A Note 1
                        end note
                        @enduml""");
            }
        }

        @Nested
        class WithLabel {
            @Test
            void createOneActivityWithSimpleDefaultNoteAndLabel() {
                String flowchart = flowchart()
                        .then(doActivity("action").with(note("A Note")).label("label"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        ->label;
                        note right
                        A Note
                        end note
                        @enduml""");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        :action;
                        ->label;
                        @enduml""");
        }

        @Nested
        class SwimLanes {
            @Test
            void createMultipleActivitiesWithLabelsInSwimLanes() {
                String flowchart = flowchart()
                        .then(andActivity("action").label("label 1").inSwimLane("S1"))
                        .then(then("action 1").label("label 2").inSwimLane("S2"))
                        .create();
                assertThat(flowchart).isEqualToNormalizingNewlines("""
                        @startuml
                        |S1|
                        |S1|
                        :action;
                        ->label 1;
                        |S2|
                        :action 1;
                        ->label 2;
                        @enduml""");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2>
                    :action3;
                    @enduml""");
        }

        @Test
        void receiveActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").receiveStyle())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2<
                    :action3;
                    @enduml""");
        }

        @Test
        void squareActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").square())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2]
                    :action3;
                    @enduml""");
        }

        @Test
        void angledActivity() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .and(doActivity("action2").angled())
                    .last(doActivity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action1;
                    :action2>
                    :action3;
                    @enduml""");
        }
    }
}
