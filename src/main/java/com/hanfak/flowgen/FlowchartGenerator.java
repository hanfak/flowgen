package com.hanfak.flowgen;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Nodes.*;
import static com.hanfak.flowgen.Theme.NONE;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.PNG;
import static net.sourceforge.plantuml.FileFormat.SVG;

public class FlowchartGenerator {

    public static final Pattern SWIM_LANE_REGEX = Pattern.compile("(\\|.+?\\|)");

    // Impl queue as linkedlist: Deque<E> queue = new LinkedList<>();
    // Store all activities put in queue, and certain actions (groups, if, repeat etc) will have their on subqueue
    // When creating string, get first item from queue (pop), if contains subqueue, do this queue unitl finished then hand back to main queue
    // Create a polymorphic type that accepts Deque and actions??
    // Traversal of queue, is just traversing a tree

    // Or maybe just use

    private final StringBuilder flowchartString = new StringBuilder();
    private final Queue<String> actions = new LinkedList<>();

    private FlowchartGenerator() {
    }

    public static FlowchartGenerator flowchart() {
        return new FlowchartGenerator().with(NONE);
    }

    public static FlowchartGenerator flowchartWith(Theme theme) {
        return new FlowchartGenerator().with(theme);
    }

    private FlowchartGenerator with(Theme theme) {
        if (!Objects.equals(theme, NONE)) {
            this.actions.add("%s%s".formatted(theme.value(), lineSeparator()));
        }
        return this;
    }

    public FlowchartGenerator withTitle(String title) {
        actions.add("title%n%s%nend title%n".formatted(title));
        return this;
    }

    public FlowchartGenerator withHeader(String header) {
        actions.add("header%n%s%nend header%n".formatted(header));
        return this;
    }

    public FlowchartGenerator withFooter(String footer) {
        actions.add("footer%n%s%nend footer%n".formatted(footer));
        return this;
    }

    public FlowchartGenerator withLegend(String legend) {
        actions.add("legend%n%s%nend legend%n".formatted(legend));
        return this;
    }

    public FlowchartGenerator withLegendRight(String legend) {
        actions.add("legend right%n%s%nend legend%n".formatted(legend));
        return this;
    }

    public FlowchartGenerator withCaption(String caption) {
        actions.add("caption%n%s%nend caption%n".formatted(caption));
        return this;
    }

    public FlowchartGenerator withLabel(String label) {
        actions.add(label(label).build());
        return this;
    }

    public FlowchartGenerator and(Action action) {
        this.actions.add(action.build());
        return this;
    }

    public FlowchartGenerator last(Action action) {
        this.actions.add(action.build());
        return this;
    }

    public FlowchartGenerator then(Action action) {
        this.actions.add(action.build());
        return this;
    }

    public FlowchartGenerator has(Action action) {
        this.actions.add(action.build());
        return this;
    }

    public FlowchartGenerator hasGroupWith(Action... actions) {
        this.actions.add(group().containing(actions).build());
        return this;
    }

    public FlowchartGenerator hasGroupWith(String name, Action... actions) {
        this.actions.add(group(name).containing(actions).build());
        return this;
    }

    public FlowchartGenerator withStartNode() {
        this.actions.add(START.build());
        return this;
    }

    public FlowchartGenerator withStopNode() {
        this.actions.add(STOP.build());
        return this;
    }

    public FlowchartGenerator withEndNode() {
        this.actions.add(END.build());
        return this;
    }

    public FlowchartGenerator withDetachedConnector(String value) {
        this.actions.add("(%s)%ndetach%n(%s)%n".formatted(value, value));
        return this;
    }


    public FlowchartGenerator withConnector(String value) {
        this.actions.add("(%s)%n".formatted(value));
        return this;
    }

    public String create() {
        String startUml = "@startuml" + lineSeparator();
        String endUml = "@enduml";

        String details = String.join("", this.actions).replaceAll("(?m)^[ \t]*\r?\n", "");
        StringBuilder start = flowchartString.append(startUml);
        Matcher matchFirstSwimLane = SWIM_LANE_REGEX.matcher(details);
        if (matchFirstSwimLane.find()) {
            start.append(matchFirstSwimLane.group(0)).append(lineSeparator());
        }
        return start.append(details).append(endUml).toString();
    }

    public String createSvg() {
        SourceStringReader reader = new SourceStringReader(create());
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            DiagramDescription diagramDescription = reader.outputImage(os, new FileFormatOption(SVG));
            if (diagramDescription.getDescription().contains("Error")) {
                throw new IllegalStateException("There is something wrong with your syntax"); // TODO: test
            }
            return os.toString(UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(); // TODO: test
        }
    }

    public void createFile(Path path) {
        String svg = createSvg();
        try {
            Files.write(path, svg.getBytes());
        } catch (IOException e) {
           throw new IllegalStateException(); // TODO:  test
        }
    }

    public void createPngFile(Path path) {
        try {
            byte[] result;
            SourceStringReader reader = new SourceStringReader(create());
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                DiagramDescription diagramDescription = reader.outputImage(os, new FileFormatOption(PNG));
                if (diagramDescription.getDescription().contains("Error")) {
                    throw new IllegalStateException("There is something wrong with your syntax"); // TODO: test
                }
                result = os.toByteArray();
            } catch (IOException e) {
                throw new IllegalStateException(); // TODO: test
            }
            Files.write(path, result);
        } catch (IOException e) {
            throw new IllegalStateException(); // TODO:  test
        }
    }
}
