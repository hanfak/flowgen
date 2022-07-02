package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.Exit.exit;

class RepeatExamples {

    @Test
    void simpleRepeatLoop() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .the(activity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .repeatWhen("is Big?")
                        .is("yes")
                        .leaveWhen("no"))
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
                                .repeatWhen("is empty?").is("yes")
                                .leaveWhen("no"))
                        .repeatWhen("is Big?").is("yes")
                        .leaveWhen("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithStartAndRepeatConnectorsHaveActions() {
        flowchart()
                .withStartNode()
                .then(repeat(activity("first action").angled())
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .repeatWhen("is Big?").is("yes")
                        .repeatLabel(doActivity("Repeat"))
                        .leaveWhen("no"))
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
                                .thenFor("yes", doActivity("action55"), doActivity("action66"), exit())
                                .orElseFor("no", doActivity("action77")))
                        .repeatWhen("is Big?").is("yes")
                        .repeatLabel(doActivity("Repeat"))
                        .leaveWhen("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void simpleRepeatLoopWithInnerIfBreak() {
        flowchart()
                .withStartNode()
                .then(repeat()
                        .and(doActivity("action1"), thenActivity("action2"))
                        .and(doActivity("action3"))
                        .and(ifIsTrue("is big?")
                                .thenFor("yes", doActivity("action55"), doActivity("action66"), leave())
                                .orElseFor("no", doActivity("action77")))
                        .repeatWhen("is Big?").is("yes")
                        .repeatLabel(doActivity("Repeat"))
                        .leaveWhen("no"))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
