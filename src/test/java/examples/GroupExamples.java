package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.MultiConditional.multiIf;
import static com.hanfak.flowgen.Note.note;
import static com.hanfak.flowgen.ParallelProcess.andDoInParallel;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.While.loopWhen;

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
                                        .an(activity("action1"))
                                        .an(activity("action2"), andDoInParallel()
                                                .the(activity("action4"), andExit())
                                                .the(activity("action5"), activity("action3"))))
                                .and(group().containing(loopWhen("is Big?")
                                        .execute(an(activity("action1").angled()).and(activity("action2").with(note("note"))))))
                                .containing(
                                        multiIf("big?")
                                                .then("yes", doActivity("action"))
                                                .elseIf("no", "condition 1?", "yes", an(activity("action1")).and(activity("action3")).and(activity("action4")))
                                                .elseIf("no", "condition 2?", "yes", doActivity("action2"))
                                                .orElse("none", doActivity("action4")),
                                        group().with(repeat()
                                                        .and(an(activity("action1")).and(activity("action2")))
                                                        .and(doActivity("action3"))
                                                        .repeatWhen("is Big?").isTrueFor("yes").labelRepeat(doActivity("Repeat"))
                                                        .exitOn("no"))
                                                .and(doActivity("action4")))))
                .withStopNode()
                .createPngFile(Paths.get("./test3.png"));
    }
}
