package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Conditional.conditional;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;

public class SynchronousActivitiesExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(activity("action1")).withLabel("then")
                .then(activity("action2")).withLabel("then next")
                .then(activity("action3")).withLabel("finally")
                .then(activity("action4"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .then(conditional("is big?")
                        .then("yes", activity("action1"), label("next"), activity("action2"))
                        .exitLabel("Carry On"))
                .then(activity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
