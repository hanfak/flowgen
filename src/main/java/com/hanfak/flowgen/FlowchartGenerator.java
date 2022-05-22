package com.hanfak.flowgen;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Nodes.*;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.SVG;
// TODO: Instead of methods starting with, just use then() and overload
//
public class FlowchartGenerator {
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

    // TODO: Add new constructor for overall config (<style>)
    public static FlowchartGenerator flowchart() {
        return new FlowchartGenerator();
    }

    // TODO: Duplicate with extra param for config
    public FlowchartGenerator withTitle(String title) { // TODO: Param should be string, and create object (TITLE) in line below
        actions.add("title %s\n".formatted(title));
        return this;
    }

    // TODO: Duplicate with extra param for config
    public FlowchartGenerator withLabel(String label) {
        actions.add(label(label).build());
        return this;
    }

    // TODO: Duplicate with extra param for config
    // TODO: Better name ie withActivity, doesAction??
    public FlowchartGenerator then(Activity activity) { // TODO: Param should be string, and create object in line below
        this.actions.add(activity.build());
        return this;
    }

    public FlowchartGenerator and(Activity activity) { // TODO: Param should be string, and create object in line below
        this.actions.add(activity.build());
        return this;
    }

    public FlowchartGenerator last(Activity activity) { // TODO: Param should be string, and create object in line below
        this.actions.add(activity.build());
        return this;
    }

    public FlowchartGenerator then(Conditional conditional) {
        this.actions.add(conditional.build());
        return this;
    }

    public FlowchartGenerator then(MultiConditional multiConditional) {
        this.actions.add(multiConditional.build());
        return this;
    }

    public FlowchartGenerator then(Repeat repeat) {
        this.actions.add(repeat.build());
        return this;
    }

    public FlowchartGenerator then(While loop) {
        this.actions.add(loop.build());
        return this;
    }

    // TODO: Duplicate with extra param for config
    public FlowchartGenerator withStartNode() {
        this.actions.add(START.build());
        return this;
    }

    // TODO: Duplicate with extra param for config
    public FlowchartGenerator withStopNode() {
        this.actions.add(STOP.build());
        return this;
    }

    // TODO: Duplicate with extra param for config
    public FlowchartGenerator withEndNode() {
        this.actions.add(END.build());
        return this;
    }

    public String create() {
        String startUml = "@startuml Activity" + lineSeparator();
        String endUml = "@enduml";
        StringBuilder finished = flowchartString
                .append(startUml)
                .append(String.join("", this.actions).replaceAll("(?m)^[ \t]*\r?\n", ""))
                .append(endUml);
        return finished.toString();
    }

    public String createSvg() {
        SourceStringReader reader = new SourceStringReader(create());
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            reader.outputImage(os, new FileFormatOption(SVG));
            return os.toString(UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    // TODO: create png image file
    public void createFile(Path path) {
        String svg = createSvg();
        try {
            Files.write(path, svg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
