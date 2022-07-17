package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public class Exit implements Action {

    public static Exit exit() {
        return new Exit();
    }

    public static Exit andExit() {
        return exit();
    }

    @Override
    public String build() {
        return "detach" + lineSeparator();
    }
}
