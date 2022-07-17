package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.ActionBuilder.following;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.ElseBuilder.elseDo;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.ParallelProcess.doInParallel;
import static com.hanfak.flowgen.Theme.CLASSIC;
import static com.hanfak.flowgen.While.check;

class ReadmeExamples {
    @Test
    void complexExample() {
        flowchartWith(CLASSIC)
                .withTitle("No Breakfast Journey")
                .withStartNode()
                .with(group("Buy")
                        .containing(an(activity("Go to shop"))
                                .then(doInParallel()
                                        .and(activity("buy butter"), and("buy jam"))
                                        .the(following(activity("buy bread")).and(ifIsTrue("is sourdough bread?")
                                                .then(doActivity("buy"))
                                                .or(elseDo(activity("ask staff for bread and buy" +
                                                                    ""))))))))
                .with(group("Cook")
                        .containing(an(activity("Put bread in toaster"))
                                .and(then("toast"))
                                .then(check("bread is toasting?").then(doActivity("wait")).leaveWhen("no"))
                                .then(doActivity("take toast and put on plate"))
                                .then(doActivity("spread butter on toast"))))
                .with(group("Dine")
                        .with(doActivity("eat toast")))
                .thenEnd()
                .createFile(Paths.get("./output.html"));
    }
}
