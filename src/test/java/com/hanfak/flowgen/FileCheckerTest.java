package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.ElseIfBuilder.elseIf;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.ifTrueFor;

class FileCheckerTest {
    @Test
    void createOneActivity() {
        Path path = Path.of("./test1.html");
        flowchart()
                .withStartNode()
                .then(ifTrueFor("size?")
                                .then("yes", doActivity("action") )
                                .then(elseIf("condition 1")
                                        .then(an(activity("action1")).and(activity("action3")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .then(elseIf("condition 2")
                                        .then(an(activity("action2")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .then(elseIf("condition 3")
                                        .then(an(activity("action3")))
                                        .elseLabel("no").elseIfLabel("yes"))
                                .orElse(elseDo(doActivity("action4")).forValue("no")))
                .withStopNode()
                .createFile(path.toString());
    }
}
