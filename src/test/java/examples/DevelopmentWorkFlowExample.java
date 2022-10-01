package examples;

import static com.hanfak.flowgen.ActionBuilder.an;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Group.group;
import static com.hanfak.flowgen.Theme.*;
import static com.hanfak.flowgen.While.loopWhile;

public class DevelopmentWorkFlowExample {

    public static void main(String... args) {
        flowchartWith(BLUEPRINT).withTitle("Feature Development Workflow")
                .withStartNode()
                .start(activity("1. Understand the problem"))
                .then(activity("2. Understand the system"))
                .then(activity("3. Acceptance level tests"))
                .then(activity("4. Plan implementation"))
                .then(loopWhile("5. Run Acceptance tests & are failing")
                        .execute(loopWhile("An Acceptance test is failing?")
                                        .execute(group("<size:20><b>6. Red/Green/Refactor cycle</b></size>").with(
                                                an(activity("Unit test failing"))
                                                        .then(doActivity("unit test passing"))
                                                        .then(doActivity("code refactored"))))
                                        .leaveWhen("All acceptance tests pass")))
                .then(activity("7. Code Review"))
                .then(activity("8. Commit Changes"))
                .withStopNode()
                .createPngFile("devWorkflow.png");
    }
}
