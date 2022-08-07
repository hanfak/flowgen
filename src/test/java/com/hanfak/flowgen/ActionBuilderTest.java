package com.hanfak.flowgen;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.hanfak.flowgen.ActionBuilder.*;
import static com.hanfak.flowgen.ActionBuilder.doA;
import static com.hanfak.flowgen.Activity.activity;
import static com.hanfak.flowgen.Activity.then;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static org.assertj.core.api.Assertions.assertThat;

class ActionBuilderTest {

    @Test
    void multipleActions() {
        String flowchart = flowchart()
                .then(an(activity("action")))
                .then(an("action"))
                .then(doActivity(activity("action")))
                .then(doActivity("action"))
                .then(doA(activity("action")))
                .then(doA("action"))
                .then(doThe(activity("action")))
                .then(doThe("action"))
                .then(doAn(activity("action")))
                .then(doAn("action"))
                .then(following(activity("action")))
                .then(following("action"))
                .then(next(activity("action")))
                .then(next("action"))
                .then(first(activity("action")))
                .then(first("action"))
                .create();

        long numberOfActionsCreated = Arrays.stream(flowchart.split("\n"))
                .filter(line -> line.contains(":action;"))
                .count();
        assertThat(numberOfActionsCreated).isEqualTo(16L);
    }

    @Test
    void multipleActionsInSameBuilder() {
        String flowchart = flowchart()
                .then(an(activity("action"))
                        .and(activity("action"))
                        .and("action")
                        .then(activity("action"))
                        .then("action"))
                .create();

        long numberOfActionsCreated = Arrays.stream(flowchart.split("\n"))
                .filter(line -> line.contains(":action;"))
                .count();
        assertThat(numberOfActionsCreated).isEqualTo(5L);
    }
}