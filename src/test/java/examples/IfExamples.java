package examples;

import com.hanfak.flowgen.Nodes;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Nodes.STOP;

public class IfExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .then("yes", doActivity("action1"), doActivity("action2"), STOP))
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
