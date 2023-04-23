package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class CandlestickFactoryTest {

    private CandlestickFactory subject;

    @BeforeEach
    public void setUp() {
        subject = new CandlestickFactory();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("generateCandlestickQuotesSource")
    public void shouldCreateCandlestick(List<Quote> quotes, Candlestick expectedCandlestick) {
        var candlestick = subject.generateCandlestick(quotes);

        assertTrue(candlestick.isPresent());
        assertEquals(expectedCandlestick, candlestick.get());
    }

    @Test
    void shouldReturnEmptyWhenQuotesEmpty() {
        List<Quote> quotes = List.of();
        var candlestick = subject.generateCandlestick(quotes);

        assertTrue(candlestick.isEmpty());
    }

    public static Stream<Arguments> generateCandlestickQuotesSource() {
        return Stream.of(
                Arguments.of(
                        Named.of("All quotes are different", List.of(
                                new Quote("ABC", 12.4, Instant.parse("2023-04-21T12:01:13.00Z")),
                                new Quote("ABC", 10, Instant.parse("2023-04-21T12:01:01.00Z")),
                                new Quote("ABC", 9, Instant.parse("2023-04-21T12:01:04.00Z")),
                                new Quote("ABC", 23.3, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:49.00Z"))
                        )),
                        Candlestick.builder()
                                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                                .openPrice(10.0)
                                .closingPrice(23.3)
                                .highPrice(25.0)
                                .lowPrice(9.0)
                                .build()
                ),
                Arguments.of(
                        Named.of("Opening timestamps have the same instant -- should return the first quote", List.of(
                                new Quote("ABC", 12.4, Instant.parse("2023-04-21T12:01:13.00Z")),
                                new Quote("ABC", 9, Instant.parse("2023-04-21T12:01:04.00Z")),
                                new Quote("ABC", 10, Instant.parse("2023-04-21T12:01:04.00Z")),
                                new Quote("ABC", 23.3, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:49.00Z"))
                        )),
                        Candlestick.builder()
                                .openTimestamp(Instant.parse("2023-04-21T12:01:04.00Z"))
                                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                                .openPrice(9.0)
                                .closingPrice(23.3)
                                .highPrice(25.0)
                                .lowPrice(9.0)
                                .build()
                ),
                Arguments.of(
                        Named.of("Closing timestamps have the same instant -- should return the last quote", List.of(
                                new Quote("ABC", 12.4, Instant.parse("2023-04-21T12:01:13.00Z")),
                                new Quote("ABC", 10, Instant.parse("2023-04-21T12:01:01.00Z")),
                                new Quote("ABC", 23.3, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 9, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:49.00Z"))
                        )),
                        Candlestick.builder()
                                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                                .openPrice(10.0)
                                .closingPrice(9.0)
                                .highPrice(25.0)
                                .lowPrice(9.0)
                                .build()
                ),
                Arguments.of(
                        Named.of("Multiple quotes with the same max price", List.of(
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:13.00Z")),
                                new Quote("ABC", 10, Instant.parse("2023-04-21T12:01:01.00Z")),
                                new Quote("ABC", 9, Instant.parse("2023-04-21T12:01:04.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:49.00Z"))
                        )),
                        Candlestick.builder()
                                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                                .openPrice(10.0)
                                .closingPrice(25.0)
                                .highPrice(25.0)
                                .lowPrice(9.0)
                                .build()
                ),
                Arguments.of(
                        Named.of("Multiple quotes with the same min price", List.of(
                                new Quote("ABC", 23.3, Instant.parse("2023-04-21T12:01:13.00Z")),
                                new Quote("ABC", 8, Instant.parse("2023-04-21T12:01:01.00Z")),
                                new Quote("ABC", 8, Instant.parse("2023-04-21T12:01:04.00Z")),
                                new Quote("ABC", 25, Instant.parse("2023-04-21T12:01:59.00Z")),
                                new Quote("ABC", 18, Instant.parse("2023-04-21T12:01:49.00Z"))
                        )),
                        Candlestick.builder()
                                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                                .openPrice(8.0)
                                .closingPrice(25.0)
                                .highPrice(25.0)
                                .lowPrice(8.0)
                                .build()
                )
        );
    }
}
