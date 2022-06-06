package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.While.loopWhen;

class ParallelExamples {

    @Test
    void nestedParallelActivitiesIncludingAsyncProcess() {
        flowchart()
                .withStartNode()
                .then(doInParallel()
                        .the(activity("action1"))
                        .the(activity("action2"), doInParallel()
                                .the(activity("action4"), exit()) // Not waiting to finish async process
                                .the(activity("action5"), activity("action3"))))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .execute(
                                loopWhen("is empty?")
                                        .execute(doActivity("action1"), doActivity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
