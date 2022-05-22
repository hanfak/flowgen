package com.hanfak.flowgen;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Conditional.conditional;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.MultiConditional.multiConditional;
import static com.hanfak.flowgen.Repeat.repeat;
import static com.hanfak.flowgen.While.loop;
import static org.assertj.core.api.Assertions.assertThat;

class FlowchartGeneratorTest {

    @Nested
    class Default {

        @Test
        void emptyFlowchart() {
            flowchart().create();
        }
    }

    @Nested
    class Title {

        // TODO: Font colour "title <color:red><size:50>Hello" -> single, multiple styling
        @Test
        void titleAtTopOfFlowchart() {
            String flowchart = flowchart()
                    .withTitle("Title")
                    .withStartNode()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    title Title
                    start
                    :action1;
                    :action2;
                    @enduml""");
        }
    }

    @Nested
    class Nodes {

        @Test
        void createOneActivityWithStartAndStopNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartAndEndNodes() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    end
                    @enduml""");
        }

        @Test
        void createOneActivityWithStartNodeOnly() {
            String flowchart = flowchart()
                    .withStartNode()
                    .thenActivity(activity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    start
                    :action;
                    @enduml""");
        }

        @Test
        void createOneActivityWithStopNodeOnly() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .withStopNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    stop
                    @enduml""");
        }

        @Test
        void createOneActivityWithEndNodeOnly() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .withEndNode()
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    end
                    @enduml""");
        }

        // TODO: Mulitple stops at different points
    }

    @Nested
    class SynchronousActivityFlow {

        @Test
        void createOneActivity() {
            String flowchart = flowchart()
                    .thenActivity(activity("action"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action;
                    @enduml""");
        }

        @Test
        void oneFlowBetweenMultipleActivities() {
            String flowchart = flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action1;
                    :action2;
                    :action3;
                    @enduml""");
        }
    }

    @Nested
    class LabelOnConnectors {
        @Test
        void labelConnectorsBetweenActions() {
            String flowchart = flowchart()
                    .thenActivity(activity("action1"))
                    .withLabel("then")
                    .thenActivity(activity("action2"))
                    .withLabel("then next")
                    .thenActivity(activity("action3"))
                    .withLabel("finally")
                    .thenActivity(activity("action4"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    :action1;
                    ->then;
                    :action2;
                    ->then next;
                    :action3;
                    ->finally;
                    :action4;
                    @enduml""");
        }
    }

    @Nested
    class ConditionalFlow {

        // TODO: No predicate outcome show for then branch
        // TODO: then without predicateOutcome
        // TODO: styling - diamond, line, colour
        @Test
        void ifELseWithPredicatesOnBothPaths() {
            String flowchart = flowchart()
                    .withConditional(conditional("is big?")
                            .then("yes", activity("action1"), activity("action3"))
                            .orElse("no", activity("action2")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (is big?) then (yes)
                    :action1;
                    :action3;
                    else (no)
                    :action2;
                    endif
                    @enduml""");
        }

        @Test
        void elsePathHasNoPredicateDefined() {
            String flowchart = flowchart()
                    .withConditional(conditional("is big?")
                            .then("yes", activity("action1"), activity("action3"))
                            .orElse(activity("action2")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (is big?) then (yes)
                    :action1;
                    :action3;
                    else
                    :action2;
                    endif
                    @enduml""");
        }

        @Test
        void ifWithoutElse() {
            String flowchart = flowchart()
                    .withConditional(conditional("is big?")
                            .then("yes", activity("action1"), activity("action3")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (is big?) then (yes)
                    :action1;
                    :action3;
                    endif
                    @enduml""");
        }

        @Test
        void ifWithoutElseLabelExitConnector() {
            String flowchart = flowchart()
                    .withConditional(conditional("is big?")
                            .then("yes", activity("action1"), activity("action3"))
                            .exitLabel("NOK")
                    )
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (is big?) then (yes)
                    :action1;
                    :action3;
                    endif
                    ->NOK;
                    @enduml""");
        }

        @Test
        void nestedIf() {
            String flowchart = flowchart()
                    .withConditional(conditional("is big?")
                            .then("yes", activity("action1"))
                            .orElse("no",
                                    conditional("is tiny?")
                                            .then("yes", activity("action2"))
                                            .orElse("no", activity("action3")),
                                    activity("action4")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (is big?) then (yes)
                    :action1;
                    else (no)
                    if (is tiny?) then (yes)
                    :action2;
                    else (no)
                    :action3;
                    endif
                    :action4;
                    endif
                    @enduml""");
        }
    }

    @Nested
    class MultipleConditional {
        // TODO: No predicate outcome show for then branch
        // TODO: No predicate outcome show for multiple then branch
        // TODO: No predicate outcome show for multiple elseif branch
        // TODO: No predicate outcome show for orelse branch
        // TODO: guard clause
        // TODO: inline styling
        @Test
        void multipleIfELseWithPredicatesOnAllPaths() {
            String flowchart = flowchart()
                    .withMultipleConditional(
                            multiConditional("big?")
                                    .then("yes",activity("action") )
                                    .elseIf("no", "condition 1", "yes", activity("action1"), activity("action3"))
                                    .elseIf("no", "condition 2", "yes", activity("action2"))
                                    .elseIf("no", "condition 3", "yes", activity("action3"))
                                    .orElse("none", activity("action4")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    if (big?) then (yes)
                    :action;
                    (no) elseif (condition 1) then (yes)
                    :action1;
                    :action3;
                    (no) elseif (condition 2) then (yes)
                    :action2;
                    (no) elseif (condition 3) then (yes)
                    :action3;
                    else (none)
                    :action4;
                    endif
                    @enduml""");
        }
    }

    @Nested
    class RepeatFlow {

        // TODO: styling - diamond, line, colour
        // TODO: Label at start of repeat action
        // TODO: isTrue not set,
        // TODO: break in repeat, merge with before next action outside of repeat
        // TODO: use not (%s) instead of arrow for exit label, rename to not
        // TODO: step builder to force correct usage

        @Test
        @Disabled("Todo")
        void simpleRepeat() {
            String flowchart = flowchart()
                    .then(repeat()
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?)
                    @enduml""");
        }

        @Test
        void simpleRepeatLabelPredicateIs() {
            String flowchart = flowchart()
                    .then(repeat()
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?").isTrueFor("yes"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    repeat while (is Big?) is (yes)
                    @enduml""");
        }

        @Test
        void simpleRepeatLabelPredicateIsFalse() {
            String flowchart = flowchart()
                    .then(repeat()
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?").isTrueFor("yes")
                            .labelRepeat(activity("This is repeated")))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:This is repeated;
                    repeat while (is Big?) is (yes)
                    @enduml""");
        }

        @Test
        void simpleRepeatWithActionOnLoopConnectorWithExitLabel() {
            String flowchart = flowchart()
                    .then(repeat()
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?").isTrueFor("yes").labelRepeat(activity("Repeat"))
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:Repeat;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
        }

        @Test
        void simpleRepeatWithActionOnLoopConnector() {
            String flowchart = flowchart()
                    .then(repeat()
                            .actions(activity("action1"), activity("action2"))
                            .actions(activity("action3"))
                            .repeatWhen("is Big?").labelRepeat( activity("Repeat"))
                            .exitOn("no"))
                    .create();
            assertThat(flowchart).isEqualToNormalizingNewlines("""
                    @startuml Activity
                    repeat
                    :action1;
                    :action2;
                    :action3;
                    backward:Repeat;
                    repeat while (is Big?) is (yes)
                    ->no;
                    @enduml""");
        }
    }

    @Nested
    class WhileFlow {
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
                    .withWhile(loop("is Big?").isTrueFor("yes").exitLabel("no")
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
    // TODO: Split each nested test class to indiviaul files
    // TODO: split processing
    // TODO: notes
    // TODO: parallel fork
    // TODO: colours on activities, box type
    // TODO: arrows, css, detach, kill, hidden
    // TODO: syling on actions etc using <color:red> etc
    // TODO: STyling on text -> add to individual words or or substring
    // TODO: General styling use of <style>...</style>
    // TODO: connectors, detach, connected
    // TODO: Grouping/partitions, config
    // TODO: config, diamond style
    // TODO: arrow direction ??? Not available yet
    // TODO: add links to activities, notes, partitions(low priority)
    // TODO: switch (low priority)
    // TODO: set PLANTUML_LIMIT_SIZE=8192 (low priority)
    // TODO: Swimlanes (v low priority)
    @Nested
    class SvgCreation {

        @Test
        void emptyFlowchart() {
            assertThat(flowchart().createSvg())
                    .containsSubsequence("Welcome to PlantUML!", "You can start with a simple UML Diagram like:")
                    .containsSubsequence("@startuml Activity", "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram() {
            String svg = flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .createSvg();

            assertThat(svg).containsSubsequence("@startuml Activity", ":action1;", ":action2;", ":action3;", "@enduml");
        }

        // TODO: Failed to generate
    }

    // TODO: Style config for whole uml

    @Nested
    class FileCreation {

        @Test
        void emptyFlowchart(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart().createFile(file);

            assertThat(Files.readAllLines(file)
                    .get(0)).contains("Welcome to PlantUML!", "You can start with a simple UML Diagram like:");
            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml Activity",
                    "@enduml");
        }

        @Test
        void oneFlowBetweenMultipleActivitiesReturnsFileWithDiagram(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("flowchart.html");
            flowchart()
                    .thenActivity(activity("action1"))
                    .thenActivity(activity("action2"))
                    .thenActivity(activity("action3"))
                    .createFile(file);

            assertThat(Files.readAllLines(file)).containsSequence(
                    "@startuml Activity",
                    ":action1;",
                    ":action2;",
                    ":action3;",
                    "@enduml");
        }

        // TODO: Failed to generate
    }
}