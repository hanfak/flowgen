package com.hanfak.flowgen;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ElseIfTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(ElseIf.class).verify();
    }
}