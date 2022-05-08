package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.multiConditional;

public class FileChecker {
    @Test
    void createOneActivity() {
        Path path = Paths.get("./test1.html");
        flowchart()
                .withStartNode()
                .withMultipleConditional(
                        multiConditional("size?")
                                .then("yes",activity("action") )
                                .elseIf("no", "condition 1", "yes", activity("action1"), activity("action3"))
                                .elseIf("no", "condition 2", "yes", activity("action2"))
                                .elseIf("no", "condition 3", "yes", activity("action3"))
                                .orElse("no", activity("action4")))
                .withStopNode()
                .createFile(path);
    }


}
