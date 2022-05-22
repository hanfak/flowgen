package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loop;

public class WhileExamples {
    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .withWhile(loop("is Big?")
                        .withActions(activity("action1"), activity("action2")))
                .thenActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .withWhile(loop("is Big?")
                        .withActions(
                                loop("is empty?")
                                        .withActions(activity("action1"), activity("action2"))))
                .thenActivity(activity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
