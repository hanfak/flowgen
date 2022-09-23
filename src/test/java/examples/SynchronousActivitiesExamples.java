package examples;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Label.label;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

class SynchronousActivitiesExamples {

    @Test
    void multiActivitiesWithLabelsOnArrows() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1")).with(label("then"))
                .then(doActivity("action2")).with(label("then next"))
                .then(doActivity("action3")).with(label("finally"))
                .then(doActivity("action4"))
                .withStopNode()
                .createFile("./test1.html");
    }

    @Test
    void multilineActivitiesWithLabelsOnArrows() {
        String multilineActivity = "action3\n" +
                                   "Another line\n" +
                                   "another line";
        flowchart()
                .withTitle("Hello")
                .withFooter("\n\nFooter") // Add extra space between footer and activity diagram
                .withHeader("Header")
                .withStartNode()
                .then(doActivity("action1" + lineSeparator() + "more lines")).with(label("then"))
                .then(doActivity("action2\nanother line\nhello")).with(label("then next"))
                .then(doActivity(multilineActivity)).with(label("finally"))
                .then(doActivity(format("action4%nmore line%nsome more%n%s", "Hello")))
                .withStopNode()
                .createFile("./test1.html");
    }

    @Test
    void multilineTitle() {
        flowchart()
                .withTitle("Hello\nnew line")
                .withStartNode()
                .then(doActivity("action1"))
                .withStopNode()
                .createFile("./test1.html");
    }

    // TODO: use creole syntax in title

    @Test
    void ifWithGuardClauseNoReturn() {
        flowchart()
                .withStartNode()
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1"), label("next"), doActivity("action2"))
                        .existLabel("Carry On"))
                .then(doActivity("action3"))
                .withLabel("label")
                .withStopNode()
                .createFile("./test1.html");
    }
}
