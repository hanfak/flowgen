package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.hanfak.flowgen.Activity.andActivity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Break.leave;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loopWhen;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class WhileFlowchartGeneratorTest {

    // TODO: P1 add label on loop connector  "backward:This is repeated;"
    // TODO: P1 Label at start of while action
    // TODO: P1 backward as last action
    // TODO: P2 arrow style after while statement, after end while
    // TODO: P2 step builder to force correct usage
    // TODO: P2 styling - diamond, line, colour

    @Test
    void simpleWhileLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?")
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
                :action8;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsTrueLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .execute(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?) is (yes)
                :action1;
                :action2;
                end while
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsFalseLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").exitLabel("no")
                        .execute(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?)
                :action1;
                :action2;
                end while (no)
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithBothLabelsLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .exitLabel("no")
                        .execute(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?) is (yes)
                :action1;
                :action2;
                end while (no)
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileLoopWithBreak() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?")
                        .execute(ifIsTrue("is big?")
                                .thenFor("yes", doActivity("action1"), doActivity("action2"), leave()))
                        .and(doActivity("action6"), andActivity("action7"))
                        .and(ifIsTrue("is big?")
                                .thenFor("yes", doActivity("action10"), leave())))
                .then(doActivity("action8"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml
                while (is Big?)
                if (is big?) then (yes)
                :action1;
                :action2;
                break
                endif
                :action6;
                :action7;
                if (is big?) then (yes)
                :action10;
                break
                endif
                end while
                :action8;
                @enduml""");
    }
}
