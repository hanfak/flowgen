package com.hanfak.flowgen;

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
        return "->%s;%n".formatted(label);
    }
}
