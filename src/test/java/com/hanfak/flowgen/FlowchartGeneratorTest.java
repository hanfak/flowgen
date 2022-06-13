package com.hanfak.flowgen;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.Theme.SPACELAB;
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
        // TODO: multi line title (not possible yet)
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
                    title
                    Title
                    end title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Caption {
        // See https://plantuml.com/commons#8413c683b4b27cc3
        // TODO: stylling on caption, location etc
        @Test
        void captionAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withCaption("Figure 1")
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    caption
                    Figure 1
                    end caption
                    title
                    Title
                    end title
                    start
                    :action1;
                    @enduml""");
        }

        @Test
        void multiLineCaptionAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withCaption("Figure 1\nAnother line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    caption
                    Figure 1
                    Another line
                    end caption
                    start
                    :action1;
                    @enduml""");
        }
    }

    @Nested
    class Nodes {
        // TODO: label on start node for preconditions
        // TODO: arrow style after start node
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
        // TODO: labels be part of Activity class?
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
                    title
                    Title
                    end title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }

        @Test
        void flowchartWithAvailablePlantUmlThemes() {
            String flowchart = flowchartWith(SPACELAB)
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    !theme spacelab
                    title
                    Title
                    end title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Connectors {
        // TODO: Styliing on connector
        @Test
        void detachedConnectorBetweenActions() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .withDetachedConnector("A")
                    .then(doActivity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    (A)
                    detach
                    (A)
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void nonDetachedConnectorBetweenActions() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .withConnector("A")
                    .then(doActivity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    (A)
                    :action;
                    stop
                    @enduml""");
        }
    }

    @Nested
    class Detach {
        @Test
        void stopAndHideArrowAfterAction() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .then(exit())
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action;
                    -[hidden]->
                    detach
                    @enduml""");
        }
    }

    @Nested
    class Header {
        // TODO: styling
        @Test
        void singleLineHeaderAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withHeader("header")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    header
                    header
                    end header
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }

        @Test
        void multiLineHeaderAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withHeader("header\nsecond line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    header
                    header
                    second line
                    end header
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Footer {
        // TODO: styling
        @Test
        void singleLineFooterAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withFooter("footer")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    footer
                    footer
                    end footer
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }

        @Test
        void multiLineHeaderAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withFooter("footer\nsecond line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    footer
                    footer
                    second line
                    end footer
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Legend {
        // TODO: styling
        @Test
        void singleLineLegendAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withLegend("legend")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    legend
                    legend
                    end legend
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }

        @Test
        void multiLineLegendAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .withLegend("legend\nsecond line")
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    start
                    :action1;
                    :action2;
                    legend
                    legend
                    second line
                    end legend
                    @enduml""");
        }

        @Test
        void legendAtBottomRightFlowchart() {
            String flowchart = flowchart()
                    .withLegendRight("legend")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml
                    legend right
                    legend
                    end legend
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    // TODO: All todos throughout code base, incl failing tests
    // TODO: legend https://plantuml.com/commons#8413c683b4b27cc3
    // TODO: zoom https://plantuml.com/commons#8413c683b4b27cc3
    // TODO: detach on activity
    // TODO: In all classes Instead of queue, use delegate to hide queue, with delegate using the queue underneath easier to change
    // TODO: In Activity, do labels
    // TODO: In Conditional, pass in factory builder
    // TODO: In Conditional,  naming of ifIsTrue() and orElse()- branchWhen()?
    // TODO: In Conditional,  and() method to chain on to then and/or orElse
    // TODO: In multiConditional, pass in factory builder
    // TODO: In multiConditional, better naming
    // TODO: In Repeat, better naming: is? repeatAgainFor? for isTrueFor; repeatLoopAction? for labelRepeat; exitLoopFor?? for exitOn
    // TODO: In while, better naming: withActivities, doesAction for execute; exitLoopFor? for exitLabel

    // NExt release
    // TODO: in FlowchartGenerator for each setter Param should be builder, and create object (xxx) in line below for multi line
    // TODO: in FlowchartGenerator for each setter duplicate and add config param
    // TODO: in FlowchartGenerator constructor can pass in <style> to allow user to pass in custom style for all elements
    // TODO: in FlowchartGenerator for setters and, then, last should pass in string param and create the activity in the method
    // TODO: In multiConditional, combine with conditional ??
    // TODO: in Activity Add a builder (Activities) implement Action ie Activities.activity("action2").thenDo("action2")
    // TODO: in Nodes, Might move enums to individual classes, to allow for styling
    // TODO: General styling use of <style>...</style>
    // TODO: arrows, kill, hidden, dotted|dashed|bold|hidden|
    // TODO: STyling on text -> add to individual words or or substring
    // TODO: Add custom themes
    // TODO: styling on actions etc using <color:red> etc
    // TODO: box type - slanted, bars


    // Further releases
    // TODO: activity uses a builder to add styling, pass in Content factory (build to string). a builder that build a multiline content (using queue) and have methods for bold, tables, list, lines etc
    // TODO: skinparams for styling
    // TODO: add links to activities, notes, partitions(low priority)
    // TODO: switch (low priority)
    // TODO: partitions
    // TODO: set PLANTUML_LIMIT_SIZE=8192 (low priority)
    // TODO: config, diamond style
    // TODO: Different file formats ie gifs see FileFormat, do some refactoring around generating instead of repetition
    // TODO: write code to generate actions, preprocessing
        // http://www.plantuml.com/plantuml/uml/LOxB2eCm44NtynNZu59ST1aHR3-XxmTT39hIW4d5n3ug_dkZBMYpkZTdEDpCEgvTeqi8migdKffqvdF1ZjEM-Y-LgugDiuJY12qPrf84qlvm98o8RL-UhpTrqOGkL-kHrjRzqyrsDzBc_g0Epj01Y7a2mULMIywl62edDIg3mvXuEWNmzyHm5FTUP8lVnjPRf2cy2CGYWSpdHfSV


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

        void emptyFlowchart(@TempDir Path tempDir) throws IOException, InterruptedException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart().createFile(file);
            Thread.sleep(2000); // TODO: Fix, use awaitly to wait for file to be created
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