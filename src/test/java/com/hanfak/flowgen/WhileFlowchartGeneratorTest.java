package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Activity.andActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loopWhen;
import static org.assertj.core.api.Assertions.assertThat;

class WhileFlowchartGeneratorTest {

    // TODO: add label on loop connector  backward:This is repeated;
    // TODO: add detach to form infinite loop (static method infniiteLoop) -[hidden]->
    // TODO: styling - diamond, line, colour
    // TODO: Label at start of while action
    // TODO: isTrue not set,
    // TODO: exitLabel not set,
    // TODO: step builder to force correct usage
    @Test
    void simpleWhileLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?")
                        .doAction(doActivity("action1"))
                        .then(doActivity("action2"))
                        .then(doActivity("action2"), andActivity("action3"))
                        .and(doActivity("action4"))
                        .and(doActivity("action5"), andActivity("action6")))
                .then(doActivity("action8"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                while (is Big?)
                :action1;
                :action2;
                :action3;
                :action4;
                :action5;
                :action6;
                endwhile
                :action7;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsTrueLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .doActions(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                while (is Big?) is (yes)
                :action1;
                :action2;
                endwhile
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsFalseLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").exitLabel("no")
                        .doActions(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                while (is Big?)
                :action1;
                :action2;
                endwhile (no)
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithBothLabelsLoop() {
        String flowchart = flowchart()
                .then(loopWhen("is Big?").isTrueFor("yes")
                        .exitLabel("no")
                        .doActions(doActivity("action1"), doActivity("action2")))
                .then(doActivity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                while (is Big?) is (yes)
                :action1;
                :action2;
                endwhile (no)
                :action3;
                @enduml""");
    }
}
