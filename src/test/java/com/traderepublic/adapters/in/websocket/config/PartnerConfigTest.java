package com.traderepublic.adapters.in.websocket.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration-test")
@SpringBootTest(properties = {
        "partner.enabled=false",
        "partner.instrument-uri=test-uri:8080/instruments",
        "partner.quotes-uri=test-uri:8888/quotes"
})
class PartnerConfigTest {

    @Autowired
    private PartnerConfig subject;

    @Test
    public void shouldLoadTheConfiguration() {
        assertFalse(subject.isEnabled());
        assertEquals("test-uri:8080/instruments", subject.getInstrumentUri());
        assertEquals("test-uri:8888/quotes", subject.getQuotesUri());
    }
}