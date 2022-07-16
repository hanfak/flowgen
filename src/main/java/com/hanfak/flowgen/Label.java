package com.hanfak.flowgen;

import static java.lang.String.format;

public class Label implements Action {

    private final String label;

    private Label(String label) {
        this.label = label;
    }

    public static Label label(String label) {
        return new Label(label);
    }

    @Override
    public String build() {
        return format("->%s;%n", label);
    }
}
