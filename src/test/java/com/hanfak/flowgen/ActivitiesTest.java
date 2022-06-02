package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;
import static org.assertj.core.api.Assertions.assertThat;

class ActivitiesTest {

    @Nested
    class SynchronousActivityFlow {

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
        void oneFlowBetweenMultipleActivities() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
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
    }

    @Nested
    class Notes {

        // TODO: styling a note
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
    }

    // TODO: Arrow styling
}
