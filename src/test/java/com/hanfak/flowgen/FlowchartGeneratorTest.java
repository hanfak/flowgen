package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
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
        // TODO: multi line title
        // TODO: Font colour "title <color:red><size:50>Hello" -> single, multiple styling
        @Test
        void titleAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    title Title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Nodes {
        // TODO: label on start node for preconditions
        @Test
        void createOneActivityWithStartAndStopNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartAndEndNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    end
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartNodeOnly() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    @enduml""");
        }

        @Test
        void createOneActivityWithStopNodeOnly() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithEndNodeOnly() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    :action;
                    end
                    @enduml""");
        }

        // TODO: Mulitple stops at different points
    }

    @Nested
    class LabelOnConnectors {
        @Test
        void labelConnectorsBetweenActions() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .withLabel("then")
                    .then(doActivity("action2"))
                    .withLabel("then next")
                    .then(doActivity("action3"))
                    .withLabel("finally")
                    .then(doActivity("action4"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
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

    @Nested
    class Themes {
        // TODO: Add to docs https://plantuml.com/theme  https://the-lum.github.io/puml-themes-gallery/
        @Test
        void flowchartHasNoThemeByDefault() {
            String flowchart = flowchart()
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    title Title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }

        @Test
        void flowchartWithAvailablePlantUmlThemes() {
            String flowchart = flowchartWith(Theme.SPACELAB)
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    !theme spacelab
                    title Title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }
    // TODO: General styling use of <style>...</style>
    // TODO: use %n,\n or lineSeparator
    // TODO: connectors, detach, connected
    // TODO: Grouping/partitions, config
    // TODO: arrows, css, detach, kill, hidden
    // TODO: Swimlanes


    // TODO: Add custom themes
    // TODO: colours on activities, box type
    // TODO: syling on actions etc using <color:red> etc
    // TODO: STyling on text -> add to individual words or or substring
    // TODO: config, diamond style

    // TODO: add links to activities, notes, partitions(low priority)
    // TODO: switch (low priority)
    // TODO: set PLANTUML_LIMIT_SIZE=8192 (low priority)

    // TODO: arrow direction ??? Not available yet

    @Nested
    class SvgCreation {

        @Test
        void emptyFlowchart() {
            assertThat(flowchart().createSvg())
                    .containsSubsequence("Welcome to PlantUML!", "You can start with a simple UML Diagram like:")
                    .containsSubsequence("@startuml", "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram() {
            String svg = flowchart()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .then(doActivity("action3"))
                    .createSvg();

            assertThat(svg).containsSubsequence("@startuml", ":action1;", ":action2;", ":action3;", "@enduml");
        }

        // TODO: Failed to generate
    }

    @Nested
    class FileCreation {

        @Test
        void emptyFlowchart(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart().createFile(file);

            assertThat(Files.readAllLines(file)
                    .get(0)).contains("Welcome to PlantUML!", "You can start with a simple UML Diagram like:");
            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml",
                    "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .then(doActivity("action3"))
                    .createFile(file);

            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml",
                    ":action1;",
                    ":action2;",
                    ":action3;",
                    "@enduml");
        }

        // TODO: Failed to generate
    }
}