package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.While.loopWhen;

// Using same builder can create as separate flowcharts in same diagram
class MultipleSeparateFlowchartsExamples {

    @Test
    void multipleNonConnectedDiagramsUsingSameBuilder() {
        flowchart()
                .withTitle("First")
                .withStartNode()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .execute(activity("action1"), thenActivity("action2")))
                .then(exit())
                .withStartNode()
                .then(repeat()
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .repeatWhen("is Big?")
                        .isTrueFor("yes")
                        .labelRepeat(doActivity("Repeat"))
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
