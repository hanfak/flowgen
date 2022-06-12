package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public class Continue implements Action {

    public static Continue skip() {
        return new Continue();
    }

    @Override
    public String build() {
        return "continue" + lineSeparator();
    }
}
