package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.MultiConditional.multiIf;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.While.loopWhen;
import static org.assertj.core.api.Assertions.assertThat;

class GroupExamples {

    @Test
    void multipleGroups() {
        flowchart()
                .withStartNode()
                .then(group("Name")
                        .with(doActivity("action1"))
                        .with(doActivity("action2")))
                .then(group("Name")
                        .with(doActivity("action3"))
                        .with(doActivity("action4")))
                .then(group("Name")
                        .with(doActivity("action5")))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedGroups() {
        flowchart()
                .withStopNode()
                .then(group("Name")
                        .with(doActivity("action1"))
                        .with(group("Name")
                                .with(doActivity("action3"))
                                .with(doActivity("action4"))))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedGroupsWithOtherComplexActions() {
        flowchart()
                .withStopNode()
                .has(group("Name")
                        .with(doActivity("action1"))
                        .with(group("Name")
                                .with(doInParallel()
                                        .the(activity("action1"))
                                        .the(activity("action2"), doInParallel()
                                                .the(activity("action4"), exit())
                                                .the(activity("action5"), activity("action3"))))
                                .and(group().with(loopWhen("is Big?")
                                        .execute(activity("action1"), thenActivity("action2"))))
                                .containing(
                                        multiIf("big?")
                                                .then("yes", doActivity("action"))
                                                .elseIf("no", "condition 1?", "yes", doActivity("action1"), andActivity("action3"))
                                                .elseIf("no", "condition 2?", "yes", doActivity("action2"))
                                                .orElse("none", doActivity("action4")),
                                        group().with(repeat()
                                                        .and(doActivity("action1"), thenActivity("action2"))
                                                        .and(doActivity("action3"))
                                                        .repeatWhen("is Big?")
                                                        .isTrueFor("yes")
                                                        .labelRepeat(doActivity("Repeat"))
                                                        .exitOn("no"))
                                                .and(doActivity("action4")))))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
