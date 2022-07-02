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
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?)
                :action1;
                :action2;
                :action3;
                :action4;
                :action5;
                :action6;
                :action7;
                end while
                -[hidden]->
                detach
                :action8;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsTrueLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes")
                        .execute(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?) is (yes)
                :action1;
                :action2;
                end while
                -[hidden]->
                detach
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithBothLabelsAndRepeatLabelLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes")
                        .perform(activity("action1"), andActivity("action2"))
                        .repeatLabel(activity("Repeated")))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?) is (yes)
                :action1;
                :action2;
                backward:Repeated;
                end while
                -[hidden]->
                detach
                @enduml""");
    }

    @Test
    void simpleWhileWithNoteLoop() {
        String flowchart = flowchart()
                .then(infiniteLoopWhen("is Big?").is("yes").with(note("A Note"))
                        .execute(doActivity("action1"), doActivity("action2"))
                        .repeatLabel(activity("Repeated")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?) is (yes)
                note right
                A Note
                end note
                :action1;
                :action2;
                backward:Repeated;
                end while
                -[hidden]->
                detach
                :action3;
                @enduml""");
    }
}
