package examples;

import com.hanfak.flowgen.Nodes;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.conditional;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;

public class IfExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(conditional("is big?")
                        .then("yes", doActivity("action1"), doActivity("action2"), Nodes.STOP))
                .withLabel("no")
                .then(doActivity("action2"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .then(conditional("is big?")
                        .then("yes", doActivity("action1"), label("next"), doActivity("action2"))
                        .exitLabel("Carry On"))
                .then(doActivity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
