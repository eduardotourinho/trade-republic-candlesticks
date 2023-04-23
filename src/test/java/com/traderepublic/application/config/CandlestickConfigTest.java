package com.traderepublic.application.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration-test")
@SpringBootTest(properties = {
        "candlesticks.aggregation-timeframe-minutes=10"
})
class CandlestickConfigTest {

    @Autowired
    private CandlestickConfig subject;

    @Test
    public void shouldLoadTheCorrectConfiguration() {
        assertEquals(10, subject.getAggregationTimeframeMinutes());
    }
}