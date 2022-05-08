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
                .withActivity(activity("action1")).withLabel("then")
                .withActivity(activity("action2")).withLabel("then next")
                .withActivity(activity("action3")).withLabel("finally")
                .withActivity(activity("action4"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), label("next"), activity("action2"))
                        .exitLabel("Carry On"))
                .withActivity(activity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
