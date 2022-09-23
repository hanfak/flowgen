package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.ActionBuilder.following;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.*;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Theme.CLASSIC;
import static com.hanfak.flowgen.While.check;

class ReadmeExamples {

    @Test
    void simpleExample() {
        flowchart()
                .then(ifThe("house is big?")
                        .then(doActivity("sell house"))
                        .or(elseDo(activity("stay"))))
                .createPngFile("./output.png");
    }

    @Test
    void complexExample() {
        flowchartWith(CLASSIC)
                .withTitle("No Breakfast At Home Journey")
                .withStartNode()
                .with(group("Buy")
                        .containing(an(activity("Go to shop"))
                                .then(doInParallel()
                                        .the(activity("buy butter"), then("buy jam"))
                                        .the(following(activity("buy bread"))
                                                .and(ifIt("is sourdough bread?")
                                                        .then(doActivity("buy"))
                                                        .or(elseDo(activity("ask staff for bread"))))))))
                .with(group("Cook")
                        .containing(an(activity("Put bread in toaster"))
                                .and(then("toast"))
                                .then(check("bread is toasting?").is("yes")
                                        .then(doActivity("wait"))
                                        .leaveWhen("no"))
                                .then(doActivity("take toast and put on plate"))
                                .then(doActivity("spread butter on toast"))))
                .with(group("Dine")
                        .with(doActivity("eat toast")))
                .thenEnd()
                .createFile("./output.html");
    }

    @Test
    void customFormattingExample() {
        flowchart()
                .withTitle("Custom Formatting")
                .with(activity("do some //code// like:\n" +
                               "\"\"foo()\"\""))
                .createFile("./output.html");
    }
}
