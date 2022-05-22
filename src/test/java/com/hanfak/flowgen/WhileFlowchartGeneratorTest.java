package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.While.loop;
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
                .withWhile(loop("is Big?")
                        .withActions(activity("action1"), activity("action2")))
                .thenActivity(activity("action3"))
                .create();
        assertThat(flowchart).isEqualToNormalizingNewlines("""
                @startuml Activity
                while (is Big?)
                :action1;
                :action2;
                endwhile
                :action3;
                @enduml""");
    }

    @Test
    void simpleWhileWithLabelForPredicateIsTrueLoop() {
        String flowchart = flowchart()
                .withWhile(loop("is Big?").isTrueFor("yes")
                        .withActions(activity("action1"), activity("action2")))
                .thenActivity(activity("action3"))
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
                .withWhile(loop("is Big?").exitLabel("no")
                        .withActions(activity("action1"), activity("action2")))
                .thenActivity(activity("action3"))
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
                .withWhile(loop("is Big?").isTrueFor("yes")
                        .exitLabel("no")
                        .withActions(activity("action1"), activity("action2")))
                .thenActivity(activity("action3"))
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
