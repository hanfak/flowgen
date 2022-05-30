package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Activity.thenActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.Exit.exit;

public class RepeatExamples {

    @Test
    void simpleRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
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
                        .and(doActivity("action1"))
                        .and(repeat()
                                .and(doActivity("action2"))
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
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .labelRepeat(doActivity("Repeat"))
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithInnerIf() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .and(ifIsTrue("is big?")
                                .then("yes", doActivity("action55"), doActivity("action66"), exit())
                                .orElse("no", doActivity("action77")))
                        .repeatWhen("is Big?").isTrueFor("yes")
                        .labelRepeat(doActivity("Repeat"))
                        .exitOn("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
