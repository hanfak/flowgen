package com.hanfak.flowgen;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ThenTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Then.class).verify();
    }
}