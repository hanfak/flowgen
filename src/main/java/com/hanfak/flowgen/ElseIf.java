package com.hanfak.flowgen;

import java.util.Queue;

record ElseIf(String predicate, String thenPredicateOutcome, String elsePredicateOutcome, Queue<Action> actions) {

}
