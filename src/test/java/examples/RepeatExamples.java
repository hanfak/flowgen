package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;

public class RepeatExamples {

    @Test
    void simpleRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .actions(activity("action1"), activity("action2"))
                        .actions(activity("action3"))
                        .repeatWhen("is Big?")
                        .isTrueFor("yes")
                        .exitOn("no"))
                .thenActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .action(activity("action1"))
                        .and(repeat()
                                .action(activity("action2"))
                                .repeatWhen("is empty?").isTrueFor("yes")
                                .exitOn("no"))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .exitOn("no"))
                .thenActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithStartAndRepeatConnectorsHaveActions() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .actions(activity("action1"), activity("action2"))
                        .action(activity("action3"))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .labelRepeat(activity("Repeat"))
                        .exitOn("no"))
                .thenActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

}
