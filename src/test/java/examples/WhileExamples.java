package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loopWhen;

public class WhileExamples {
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
