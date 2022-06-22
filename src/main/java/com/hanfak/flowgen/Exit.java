package com.hanfak.flowgen;

import static java.lang.System.lineSeparator;

public class Exit implements Action {

    public static Exit exit() {
        return new Exit();
    }

    public static Exit andExit() {
        return new Exit();
    }

    @Override
    public String build() {
        return "-[hidden]->" + lineSeparator() + "detach" + lineSeparator();
    }
}
