package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public class Break implements Action {

    public static Break leave() {
        return new Break();
    }

    @Override
    public String build() {
        return "break" + lineSeparator();
    }
}
