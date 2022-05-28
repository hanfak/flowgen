package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.multiIf;

public class FileCheckerTest {
    @Test
    void createOneActivity() {
        Path path = Paths.get("./test1.html");
        flowchart()
                .withStartNode()
                .then(
                        multiIf("size?")
                                .then("yes", doActivity("action") )
                                .elseIf("no", "condition 1", "yes", doActivity("action1"), doActivity("action3"))
                                .elseIf("no", "condition 2", "yes", doActivity("action2"))
                                .elseIf("no", "condition 3", "yes", doActivity("action3"))
                                .orElse("no", doActivity("action4")))
                .withStopNode()
                .createFile(path);
    }


}
