package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Exit.exit;
import static com.hanfak.flowgen.Nodes.STOP;
import static com.hanfak.flowgen.While.loopWhen;

class WhileExamples {

    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .execute(activity("action1"), thenActivity("action2")))
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
