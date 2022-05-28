package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;

public class SynchronousActivitiesExamples {

    @Test
    void multiActivitiesWithLabelsOnArrows() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1")).withLabel("then")
                .then(doActivity("action2")).withLabel("then next")
                .then(doActivity("action3")).withLabel("finally")
                .then(doActivity("action4"))
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
