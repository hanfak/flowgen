package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;
import static com.hanfak.flowgen.While.loopWhen;

class WhileExamples {

    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .execute(
                                an(activity("action1").label("label").with(note("comment")))
                                        .then(activity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void infiniteWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .execute(activity("action1"), thenActivity("action2")))
                .then(exit())
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void breakInWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .execute(activity("action1"), leave()))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .execute(
                                loopWhen("is empty?")
                                        .execute(activity("action1"), thenActivity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
