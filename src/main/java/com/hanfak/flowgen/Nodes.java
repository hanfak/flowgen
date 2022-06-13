package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public enum Nodes implements Action {
    START("start" + lineSeparator()),
    STOP("stop" + lineSeparator()),
    END("end" + lineSeparator());

    private final String value;

    Nodes(String value) {
        this.value = value;
    }

    @Override
    public String build() {
        return value;
    }
}
