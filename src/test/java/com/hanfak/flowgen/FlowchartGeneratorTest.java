package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

class FlowchartGeneratorTest {

    @Nested
    class Default {

        @Test
        void emptyFlowchart() {
            flowchart().create();
        }
    }

    @Nested
    class Title {

        // TODO: Font colour "title <color:red><size:50>Hello" -> single, multiple styling
        @Test
        void titleAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withTitle("Title")
                    .withStartNode()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    title Title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Nodes {

        @Test
        void createOneActivityWithStartAndStopNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartAndEndNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    end
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartNodeOnly() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    @enduml""");
        }

        @Test
        void createOneActivityWithStopNodeOnly() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithEndNodeOnly() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    end
                    @enduml""");
        }

        // TODO: Mulitple stops at different points
    }

    @Nested
    class SynchronousActivityFlow {

        @Test
        void createOneActivity() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    @enduml""");
        }

        @Test
        void oneFlowBetweenMultipleActivities() {
            String flowchart = flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action1;
                    :action2;
                    :action3;
                    @enduml""");
        }
    }

    @Nested
    class LabelOnConnectors {
        @Test
        void labelConnectorsBetweenActions() {
            String flowchart = flowchart()
                    .thenActivity(activity("action1"))
                    .withLabel("then")
                    .thenActivity(activity("action2"))
                    .withLabel("then next")
                    .thenActivity(activity("action3"))
                    .withLabel("finally")
                    .thenActivity(activity("action4"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action1;
                    ->then;
                    :action2;
                    ->then next;
                    :action3;
                    ->finally;
                    :action4;
                    @enduml""");
        }
    }

    // TODO: Split each nested test class to indiviaul files
    // TODO: split processing
    // TODO: notes
    // TODO: parallel fork
    // TODO: colours on activities, box type
    // TODO: arrows, css, detach, kill, hidden
    // TODO: syling on actions etc using <color:red> etc
    // TODO: STyling on text -> add to individual words or or substring
    // TODO: General styling use of <style>...</style>
    // TODO: connectors, detach, connected
    // TODO: Grouping/partitions, config
    // TODO: config, diamond style
    // TODO: arrow direction ??? Not available yet
    // TODO: add links to activities, notes, partitions(low priority)
    // TODO: switch (low priority)
    // TODO: set PLANTUML_LIMIT_SIZE=8192 (low priority)
    // TODO: Swimlanes (v low priority)
    @Nested
    class SvgCreation {

        @Test
        void emptyFlowchart() {
            assertThat(flowchart().createSvg())
                    .containsSubsequence("Welcome to PlantUML!", "You can start with a simple UML Diagram like:")
                    .containsSubsequence("@startuml Activity", "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram() {
            String svg = flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .createSvg();

            assertThat(svg).containsSubsequence("@startuml Activity", ":action1;", ":action2;", ":action3;", "@enduml");
        }

        // TODO: Failed to generate
    }

    // TODO: Style config for whole uml

    @Nested
    class FileCreation {

        @Test
        void emptyFlowchart(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart().createFile(file);

            assertThat(Files.readAllLines(file)
                    .get(0)).contains("Welcome to PlantUML!", "You can start with a simple UML Diagram like:");
            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml Activity",
                    "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .createFile(file);

            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml Activity",
                    ":action1;",
                    ":action2;",
                    ":action3;",
                    "@enduml");
        }

        // TODO: Failed to generate
    }
}