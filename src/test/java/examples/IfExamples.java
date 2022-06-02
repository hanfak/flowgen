package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Exit.exit;

public class IfExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello\nbye")
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .then("yes", doActivity("action1"), doActivity("action2"), exit()))
                .withLabel("no")
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
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
