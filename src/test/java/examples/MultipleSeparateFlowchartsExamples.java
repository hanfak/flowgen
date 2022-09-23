package examples;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.InfiniteLoop.infiniteLoopWhen;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Repeat.repeat;

// Using same builder can create as separate flowcharts in same diagram
class MultipleSeparateFlowchartsExamples {

    @Test
    void multipleNonConnectedDiagramsUsingSameBuilder() {
        flowchart()
                .withTitle("First")
                .withStartNode()
                .then(infiniteLoopWhen("is true?").is("yes")
                        .execute(activity("action1"), thenActivity("action2")))
                .then(doActivity("action22"))
                .withStopNode()
                .withStartNode()
                .then(repeat()
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .repeatWhen("is Big?")
                        .is("yes")
                        .repeatLabel(doActivity("Repeat"))
                        .leaveWhen("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .withStartNode()
                .then(doInParallel()
                        .the(activity("action1"))
                        .the(activity("action2"), doInParallel()
                                .the(activity("action4"), exit())
                                .the(activity("action5"), activity("action3"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile("./test1.html");
    }
}
