package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.*;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.InfiniteLoop.infiniteLoopWhen;
import static com.hanfak.flowgen.Note.note;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class InfiniteLoopFlowchartGeneratorTest {

    // TODO: P2 arrow style after while statement, after end while
    // TODO: P2 step builder to force correct usage
    // TODO: P2 styling - diamond, line, colour

    @Test
    void simpleWhileLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?")
                        .execute(doActivity("action1"))
                        .then(doActivity("action2"))
                        .then(doActivity("action3"), andActivity("action4"))
                        .and(doActivity("action5"))
                        .and(doActivity("action6"), andActivity("action7")))
                .then(doActivity("action8"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "while (is Big?)\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           ":action3;\n" +
                                                           ":action4;\n" +
                                                           ":action5;\n" +
                                                           ":action6;\n" +
                                                           ":action7;\n" +
                                                           "end while\n" +
                                                           "-[hidden]->\n" +
                                                           "detach\n" +
                                                           ":action8;\n" +
                                                           "@enduml");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsTrueLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes")
                        .execute(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "while (is Big?) is (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "end while\n" +
                                                           "-[hidden]->\n" +
                                                           "detach\n" +
                                                           ":action3;\n" +
                                                           "@enduml");
    }

    @Test
    void simpleWhileWithBothLabelsAndRepeatLabelLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes")
                        .perform(activity("action1"), andActivity("action2"))
                        .repeatLabel(activity("Repeated")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "while (is Big?) is (yes)\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "backward:Repeated;\n" +
                                                           "end while\n" +
                                                           "-[hidden]->\n" +
                                                           "detach\n" +
                                                           "@enduml");
    }

    @Test
    void simpleWhileWithNoteLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes").with(note("A Note"))
                        .execute(doActivity("action1"), doActivity("action2"))
                        .repeatLabel(activity("Repeated")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("@startuml\n" +
                                                           "while (is Big?) is (yes)\n" +
                                                           "note right\n" +
                                                           "A Note\n" +
                                                           "end note\n" +
                                                           ":action1;\n" +
                                                           ":action2;\n" +
                                                           "backward:Repeated;\n" +
                                                           "end while\n" +
                                                           "-[hidden]->\n" +
                                                           "detach\n" +
                                                           ":action3;\n" +
                                                           "@enduml");
    }
}
