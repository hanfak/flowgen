package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loopWhen;

public class WhileExamples {
    @Test
    void simpleWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .doActions(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void nestedWhileLoop() {
        flowchart()
                .withStartNode()
                .then(loopWhen("is Big?")
                        .doAction(
                                loopWhen("is empty?")
                                        .doActions(doActivity("action1"), doActivity("action2"))))
                .then(doActivity("action3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
