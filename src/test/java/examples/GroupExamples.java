package examples;

import com.hanfak.flowgen.Conditional;
import com.hanfak.flowgen.ElseBuilder;
import com.hanfak.flowgen.Theme;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.ActionBuilder.following;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.Conditional.ifThe;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.Exit.andExit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.ElseIfBuilder.elseIf;
import static com.hanfak.flowgen.MultiConditional.ifTrueFor;
import static com.hanfak.flowgen.Note.note;
import static com.hanfak.flowgen.ParallelProcess.andDoInParallel;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.Theme.CLASSIC;
import static com.hanfak.flowgen.While.check;
import static com.hanfak.flowgen.While.loopWhen;

class GroupExamples {

    @Test
    void multipleGroups() {
        flowchartWith(CLASSIC)
                .withTitle("No Breakfast Journey")
                .withStartNode()
                .then(group("Buy")
                        .containing(an(activity("Go to shop"))
                                .then(doInParallel()
                                        .and(activity("buy butter"), and("buy jam"))
                                        .the(following(activity("buy bread")).and(ifIsTrue("is sourdough bread?")
                                                .then(doActivity("buy"))
                                                .or(elseDo(activity("ask staff for bread"))))))))
                .then(group("Cook")
                        .containing(an(activity("Put bread in toaster"))
                                .and(then("toast"))
                                .then(check("bread is toasting?").then(doActivity("wait")).leaveWhen("no"))
                                .then(doActivity("take toast and put on plate"))
                                .then(doActivity("spread butter on toast"))))
                .then(group("Dine")
                        .with(doActivity("eat toast")))
                .thenEnd()
                .createFile(Paths.get("./output.html"));
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
                .createPngFile(Paths.get("./test3.png"));
    }
}
