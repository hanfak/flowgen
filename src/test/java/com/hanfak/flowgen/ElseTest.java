package com.hanfak.flowgen;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ElseTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(Else.class)
                .verify();
    }
}