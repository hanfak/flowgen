package examples;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.ElseIfBuilder.elseIf;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.MultiConditional.ifTrueFor;
import static com.hanfak.flowgen.Note.note;
import static com.hanfak.flowgen.ParallelProcess.andDoInParallel;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.While.loopWhile;

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
                .createFile("./test1.html");
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
                .createFile("./test1.html");
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
                                .and(group().containing(loopWhile("is Big?")
                                        .execute(an(activity("action1").angled()).and(activity("action2").with(note("note"))))))
                                .containing(
                                        ifTrueFor("big?")
                                                .then("yes", doActivity("action"))
                                                .then(elseIf("condition 1")
                                                        .then(an(activity("action1")).and(activity("action3")))
                                                        .elseLabel("no").elseIfLabel("yes"))
                                                .then(elseIf("condition 2")
                                                        .then(an(activity("action2")))
                                                        .elseLabel("no").elseIfLabel("yes"))
                                                .then(elseIf("condition 3")
                                                        .then(an(activity("action3")))
                                                        .elseLabel("no").elseIfLabel("yes"))
                                                .orElse(elseDo(doActivity("action4")).forValue("none")),
                                        group().with(repeat()
                                                        .and(an(activity("action1")).and(activity("action2")))
                                                        .and(doActivity("action3"))
                                                        .repeatWhen("is Big?").is("yes").repeatLabel(doActivity("Repeat"))
                                                        .leaveWhen("no"))
                                                .and(doActivity("action4")))))
                .withStopNode()
                .createPngFile("./test3.png");
    }
}
