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
                .withRepeat(repeat()
                        .withActions(activity("action1"), activity("action2"))
                        .withActions(activity("action3"))
                        .where("is Big?").isTrueFor("yes")
                        .exitLabel("no"))
                .withActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithStartAndRepeatConnectorsHaveActions() {
        flowchart()
                .withStartNode()
                .withRepeat(repeat()
                        .withActions(activity("action1"), activity("action2"))
                        .withActions(activity("action3"))
                        .where("is Big?").isTrueFor("yes").labelRepeat(activity("Repeat"))
                        .exitLabel("no"))
                .withActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

}
