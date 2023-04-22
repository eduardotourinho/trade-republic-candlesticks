package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CandlestickFactoryTest {

    private CandlestickFactory subject;

    @BeforeEach
    public void setUp() {
        subject = new CandlestickFactory();
    }

    @Test
    public void shouldCreateCandlestick() {
        var quotes = List.of(
            new Quote("ABC", 12.4, Instant.parse("2023-04-21T12:01:13.00Z")),
            new Quote("ABC", 10, Instant.parse("2023-04-21T12:01:01.00Z")),
            new Quote("ABC", 9, Instant.parse("2023-04-21T12:01:04.00Z")),
            new Quote("ABC", 23.3, Instant.parse("2023-04-21T12:01:59.00Z")),
            new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:49.00Z"))
        );

        var candlestick = subject.generateCandlestick(quotes);

        var expectedCandlestick = Candlestick.builder()
                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                .openPrice(10.0)
                .closingPrice(23.3)
                .highPrice(25.0)
                .lowPrice(9.0)
                .build();

        assertTrue(candlestick.isPresent());
        assertEquals(expectedCandlestick, candlestick.get());
    }

    @Test
    void shouldCandlestickNotBePresent() {
        List<Quote> quotes = List.of();
        var candlestick = subject.generateCandlestick(quotes);

        assertTrue(candlestick.isEmpty());
    }
}
