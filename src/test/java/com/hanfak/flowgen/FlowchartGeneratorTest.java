package com.hanfak.flowgen;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

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

@Execution(ExecutionMode.CONCURRENT)
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "title\n" +
                                                               "Title\n" +
                                                               "end title\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "caption\n" +
                                                               "Figure 1\n" +
                                                               "end caption\n" +
                                                               "title\n" +
                                                               "Title\n" +
                                                               "end title\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               "@enduml");
        }

        @Test
        void multiLineCaptionAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withCaption("Figure 1\nAnother line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "caption\n" +
                                                               "Figure 1\n" +
                                                               "Another line\n" +
                                                               "end caption\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "stop\n" +
                                                               "@enduml");
        }

        @Test
        void createOneActivityWithStartAndEndNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .thenEnd()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "end\n" +
                                                               "@enduml");
        }

        @Test
        void createOneActivityWithStartNodeOnly() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "@enduml");
        }

        @Test
        void createOneActivityWithStopNodeOnly() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action;\n" +
                                                               "stop\n" +
                                                               "@enduml");
        }

        @Test
        void createOneActivityWithEndNodeOnly() {
            String flowchart = flowchart()
                    .then(doActivity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action;\n" +
                                                               "end\n" +
                                                               "@enduml");
        }

        // TODO: Mulitple stops at different points
    }

    @Nested
    class LabelOnConnectors {

        @Test
        void labelConnectorsBetweenActionsUsingName() {
            String flowchart = flowchart()
                    .then(doActivity("action1"))
                    .withLabel("then")
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "->then;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
        }

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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               ":action1;\n" +
                                                               "->then;\n" +
                                                               ":action2;\n" +
                                                               "->then next;\n" +
                                                               ":action3;\n" +
                                                               "->finally;\n" +
                                                               ":action4;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "title\n" +
                                                               "Title\n" +
                                                               "end title\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
        }

        @Test
        void flowchartWithAvailablePlantUmlThemes() {
            String flowchart = flowchartWith(SPACELAB)
                    .withTitle("Title")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "!theme spacelab\n" +
                                                               "title\n" +
                                                               "Title\n" +
                                                               "end title\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "(A)\n" +
                                                               "detach\n" +
                                                               "(A)\n" +
                                                               ":action;\n" +
                                                               "stop\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "(A)\n" +
                                                               ":action;\n" +
                                                               "stop\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action;\n" +
                                                               "detach\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "header\n" +
                                                               "header\n" +
                                                               "end header\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
        }

        @Test
        void multiLineHeaderAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withHeader("header\nsecond line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "header\n" +
                                                               "header\n" +
                                                               "second line\n" +
                                                               "end header\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "footer\n" +
                                                               "footer\n" +
                                                               "end footer\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
        }

        @Test
        void multiLineHeaderAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withFooter("footer\nsecond line")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "footer\n" +
                                                               "footer\n" +
                                                               "second line\n" +
                                                               "end footer\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
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
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "legend\n" +
                                                               "legend\n" +
                                                               "end legend\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
        }

        @Test
        void multiLineLegendAtBottomOfFlowchart() {
            String flowchart = flowchart()
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .withLegend("legend\nsecond line")
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "legend\n" +
                                                               "legend\n" +
                                                               "second line\n" +
                                                               "end legend\n" +
                                                               "@enduml");
        }

        @Test
        void legendAtBottomRightFlowchart() {
            String flowchart = flowchart()
                    .withLegendRight("legend")
                    .withStartNode()
                    .then(doActivity("action1"))
                    .then(doActivity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                               "legend right\n" +
                                                               "legend\n" +
                                                               "end legend\n" +
                                                               "start\n" +
                                                               ":action1;\n" +
                                                               ":action2;\n" +
                                                               "@enduml");
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