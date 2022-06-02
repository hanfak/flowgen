package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

public class SynchronousActivitiesExamples {

    @Test
    void multiActivitiesWithLabelsOnArrows() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1")).withLabel("then")
                .then(doActivity("action2")).withLabel("then next")
                .then(doActivity("action3")).withLabel("finally")
                .then(doActivity("action4"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void multilineActivitiesWithLabelsOnArrows() {
        String multilineActivity = """
                action3
                Another line
                another line""";
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1" + lineSeparator() + "more lines")).withLabel("then")
                .then(doActivity("action2\nanother line\nhello")).withLabel("then next")
                .then(doActivity(multilineActivity)).withLabel("finally")
                .then(doActivity(format("action4%nmore line%nsome more%n%s", "Hello")))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Test
    void multilineTitle() {
        flowchart()
                .withTitle("Hello\nnew line")
                .withStartNode()
                .then(doActivity("action1"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    // TODO: use creole syntax in title

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .then("yes", doActivity("action1"), label("next"), doActivity("action2"))
                        .exitLabel("Carry On"))
                .then(doActivity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
