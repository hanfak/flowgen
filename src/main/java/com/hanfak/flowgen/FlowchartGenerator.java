package com.hanfak.flowgen;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Nodes.*;
import static com.hanfak.flowgen.Theme.NONE;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.PNG;
import static net.sourceforge.plantuml.FileFormat.SVG;

public class FlowchartGenerator {

    private static final Pattern SWIM_LANE_REGEX = Pattern.compile("(\\|.+?\\|)");

    // Impl queue as linkedlist: Deque<E> queue = new LinkedList<>();
    // Store all activities put in queue, and certain actions (groups, if, repeat etc) will have their on subqueue
    // When creating string, get first item from queue (pop), if contains subqueue, do this queue unitl finished then hand back to main queue
    // Create a polymorphic type that accepts Deque and actions??
    // Traversal of queue, is just traversing a tree

    // Or maybe just use

    private final StringBuilder flowchartString = new StringBuilder();
    private final Actions actions;

    private final Function<String, SourceStringReader> sourceStringReaderCreator;

    FlowchartGenerator(Actions actions, Function<String, SourceStringReader> sourceStringReaderCreator) {
        this.actions = actions;
        this.sourceStringReaderCreator = sourceStringReaderCreator;
    }

    public static FlowchartGenerator flowchart() {
        return new FlowchartGenerator(new Actions(), SourceStringReader::new).with(NONE);
    }

    public static FlowchartGenerator flowchartWith(Theme theme) {
        return new FlowchartGenerator(new Actions(), SourceStringReader::new).with(theme);
    }

    private FlowchartGenerator with(Theme theme) {
        if (!Objects.equals(theme, NONE)) {
            this.actions.add(() -> format("%s%s", theme.value(), lineSeparator()));
        }
        return this;
    }

    public FlowchartGenerator withTitle(String title) {
        actions.add(() -> format("title%n%s%nend title%n", title));
        return this;
    }

    public FlowchartGenerator withHeader(String header) {
        actions.add(() -> format("header%n%s%nend header%n", header));
        return this;
    }

    public FlowchartGenerator withFooter(String footer) {
        actions.add(() -> format("footer%n%s%nend footer%n", footer));
        return this;
    }

    public FlowchartGenerator withLegend(String legend) {
        actions.add(() -> format("legend%n%s%nend legend%n", legend));
        return this;
    }

    public FlowchartGenerator withLegendRight(String legend) {
        actions.add(() -> format("legend right%n%s%nend legend%n", legend));
        return this;
    }

    public FlowchartGenerator withCaption(String caption) {
        actions.add(() -> format("caption%n%s%nend caption%n", caption));
        return this;
    }

    public FlowchartGenerator withLabel(String label) {
        actions.add(label(label));
        return this;
    }

    public FlowchartGenerator with(Action action) {
        actions.add(action);
        return this;
    }

    public FlowchartGenerator and(Action action) {
        return with(action);
    }

    public FlowchartGenerator last(Action action) {
        return with(action);
    }

    public FlowchartGenerator then(Action action) {
        return with(action);
    }

    public FlowchartGenerator start(Action action) {
        return with(action);
    }

    public FlowchartGenerator has(Group group) {
        this.actions.add(group);
        return this;
    }

    public FlowchartGenerator hasGroupWith(Action... actions) {
        this.actions.add(group().containing(actions));
        return this;
    }

    public FlowchartGenerator hasGroupWith(String name, Action... actions) {
        this.actions.add(group(name).containing(actions));
        return this;
    }

    public FlowchartGenerator withStartNode() {
        this.actions.add(START);
        return this;
    }

    public FlowchartGenerator withStopNode() {
        this.actions.add(STOP);
        return this;
    }

    public FlowchartGenerator withEndNode() {
        this.actions.add(END);
        return this;
    }

    public FlowchartGenerator thenEnd() {
        return withEndNode();
    }

    public FlowchartGenerator withDetachedConnector(String value) {
        this.actions.add(() -> format("(%s)%ndetach%n(%s)%n", value, value));
        return this;
    }

    public FlowchartGenerator withConnector(String value) {
        this.actions.add(() -> format("(%s)%n", value));
        return this;
    }

    public String create() {
        String details = actions.combineAllActions();
        StringBuilder start = flowchartString.append("@startuml").append(lineSeparator());
        Matcher matchFirstSwimLane = SWIM_LANE_REGEX.matcher(details);
        if (matchFirstSwimLane.find()) {
            start.append(matchFirstSwimLane.group(0)).append(lineSeparator());
        }
        return start.append("skinparam dpi 200 \n").append(details).append("@enduml").toString();
    }

    public String createSvg() {
        SourceStringReader reader = sourceStringReaderCreator.apply(create());

        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            DiagramDescription diagramDescription = reader.outputImage(os, new FileFormatOption(SVG));
            if (diagramDescription.getDescription().contains("Error")) {
                throw new IllegalStateException("There is something wrong with your syntax");
            }
            return os.toString(UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Issue generating SVG", e);
        }
    }

    public void createFile(String path) {
        String svg = createSvg();
        try {
            Files.write(Path.of(path), svg.getBytes());
        } catch (IOException e) {
           throw new IllegalStateException("Issue creating file", e);
        }
    }

    public void createPngFile(String path) {
        try {
            byte[] result;
            String t = create();
            SourceStringReader reader = sourceStringReaderCreator.apply(t);
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                DiagramDescription diagramDescription = reader.outputImage(os, new FileFormatOption(PNG));
                if (diagramDescription.getDescription().contains("Error")) {
                    throw new IllegalStateException("There is something wrong with your syntax");
                }
                result = os.toByteArray();
            } catch (IOException e) {
                throw new IllegalStateException("Issue generating PNG", e);
            }
            Files.write(Path.of(path), result);
        } catch (IOException e) {
            throw new IllegalStateException("Issue creating file", e);
        }
    }
}
