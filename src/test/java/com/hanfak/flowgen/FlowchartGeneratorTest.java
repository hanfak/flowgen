package com.hanfak.flowgen;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Theme.SPACELAB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                    .with(label("then"))
                    .then(doActivity("action2"))
                    .with(label("then next"))
                    .then(doActivity("action3"))
                    .with(label("finally"))
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

    @Nested
    class SvgCreation {

        private final Function<String, SourceStringReader> sourceStringReaderFunction = SourceStringReaderStub::new;
        private final FlowchartGenerator flowchartGenerator = new FlowchartGenerator(new Actions(), sourceStringReaderFunction);

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

        @Test
        void shouldThrowExceptionIfCannotGenerateSvg() {
            assertThatThrownBy(() -> flowchartGenerator.then(doActivity("action1")).createSvg())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Issue generating SVG")
                    .getCause().isInstanceOf(IOException.class);
        }

        @Test
        void shouldThrowExceptionIfPlantUmlIsIncorrect() {
            assertThatThrownBy(() -> flowchartGenerator.then(doActivity("action2")).createSvg())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("There is something wrong with your syntax");
        }

    }

    @Nested
    class FileCreation {

        @Test
        @Disabled
        void emptyFlowchart(@TempDir Path tempDir) throws IOException, InterruptedException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart().createFile(file);
            Thread.sleep(2000); // TODO: P1 Fix, use awaitly to wait for file to be created
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

        @Test
        void shouldThrowExceptionWhenProblemGeneratingFile() {
            assertThatThrownBy(() -> flowchart().then(doActivity("action1")).createFile(Path.of("")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Issue creating file")
                    .getCause().isInstanceOf(IOException.class);
        }
    }

    @Nested
    class PngFileCreation {

        private final Function<String, SourceStringReader> sourceStringReaderFunction = SourceStringReaderStub::new;
        private final FlowchartGenerator flowchartGenerator = new FlowchartGenerator(new Actions(), sourceStringReaderFunction);

        @Test
        void createsPng(@TempDir Path tempDir) {
            Path file = tempDir.resolve("flowchart.png");

            flowchart().then(doActivity("action3")).createPngFile(file);

            assertThat(Files.exists(file)).isTrue();
        }

        @Test
        void shouldThrowExceptionWhenProblemGeneratingFile() {
            assertThatThrownBy(() -> flowchart().then(doActivity("action2")).createPngFile(Path.of("")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Issue creating file")
                    .getCause().isInstanceOf(IOException.class);
        }

        @Test
        void shouldThrowExceptionIfCannotGenerateSvg() {
            assertThatThrownBy(() -> flowchartGenerator.then(doActivity("action1")).createPngFile(Paths.get("./test2.png")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Issue generating PNG")
                    .getCause().isInstanceOf(IOException.class);
        }

        @Test
        void shouldThrowExceptionIfPlantUmlIsIncorrect() {
            assertThatThrownBy(() -> flowchartGenerator.then(doActivity("action2")).createPngFile(Paths.get("./test2.png")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("There is something wrong with your syntax");
        }
    }

    private static class SourceStringReaderStub extends SourceStringReader {

        private final String plantuml;

        public SourceStringReaderStub(String plantuml) {
            super(plantuml);
            this.plantuml = plantuml;
        }

        @Override
        public DiagramDescription outputImage(OutputStream os, FileFormatOption fileFormatOption) throws IOException {
            if (plantuml.contains(":action1;")) {
                throw new IOException();
            }
            if (plantuml.contains(":action2;")) {
                return new DiagramDescription("Error");
            }
            return this.outputImage(os,fileFormatOption);
        }
    }
}