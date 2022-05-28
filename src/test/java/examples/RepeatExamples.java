package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;

public class RepeatExamples {

    @Test
    void simpleRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .execute(doActivity("action1"), doActivity("action2"))
                        .execute(doActivity("action3"))
                        .repeatWhen("is Big?")
                        .isTrueFor("yes")
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .execute(doActivity("action1"))
                        .and(repeat()
                                .execute(doActivity("action2"))
                                .repeatWhen("is empty?").isTrueFor("yes")
                                .exitOn("no"))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithStartAndRepeatConnectorsHaveActions() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .execute(doActivity("action1"), doActivity("action2"))
                        .execute(doActivity("action3"))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .labelRepeat(doActivity("Repeat"))
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

}
