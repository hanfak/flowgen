package examples;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;
import static com.hanfak.flowgen.ThenBuilder.forValue;
import static com.hanfak.flowgen.While.loopWhile;

class WhileExamples {

    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhile("is Big?")
                        .execute(an(activity("action1").label("label").with(note("comment")))
                                        .then(activity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile("./test1.html");
    }

    @Test
    void breakInWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhile("is Big?").is("yes")
                        .execute(
                                doActivity("action1"),
                                ifIsTrue("is too big?")
                                        .then(forValue("yes")
                                                .then(doActivity("action2"))
                                                .and(activity("action3"))
                                                .and(leave())),
                                doActivity("action13")))
                        .then(doActivity("action"))
                        .withStopNode()
                        .createFile("./test1.html");
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhile("is Big?")
                        .execute(
                                loopWhile("is empty?")
                                        .execute(activity("action1"), thenActivity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile("./test1.html");
    }
}
