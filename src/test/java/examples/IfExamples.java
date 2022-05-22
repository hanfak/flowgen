package examples;

import com.hanfak.flowgen.Nodes;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Conditional.conditional;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;

public class IfExamples {

    @Test
    void ifWithGuardClauseReturnEarly() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .withConditional(conditional("is big?")
                        .then("yes", activity("action1"), activity("action2"), Nodes.STOP))
                .withLabel("no")
                .thenActivity(activity("action2"))
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
                .thenActivity(activity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
