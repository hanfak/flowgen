package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ThenBuilder.forValue;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Exit.exit;

class IfExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello\nbye")
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .then(forValue("yes")
                                .then(doActivity("action1"))
                                .and(activity("action3"))
                                .and(exit())))
                .with(label("no"))
                .then(doActivity("action2"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .then("yes", doActivity("action1"), label("next"), doActivity("action2"))
                        .exitLabel("Carry On"))
                .then(doActivity("action3"))
                .with(label("label"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
