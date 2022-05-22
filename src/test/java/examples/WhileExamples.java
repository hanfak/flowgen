package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loopWhen;

public class WhileExamples {
    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .actions(activity("action1"), activity("action2")))
                .then(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .actions(
                                loopWhen("is empty?")
                                        .actions(activity("action1"), activity("action2"))))
                .then(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
