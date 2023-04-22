package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import com.traderepublic.application.ports.out.QuoteFinderPort;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandlestickServiceTest {

    @Mock
    private QuoteFinderPort quoteFinder;
    @Mock
    private CandlestickFactory candlestickFactory;

    @InjectMocks
    private CandlestickService subject;

    @Test
    void shouldReturnEmptyIfNoQuotesInPeriod() {
        var isin = "ABC";

        when(quoteFinder.fetchQuotes(isin, 30))
                .thenReturn(List.of());

        var actualCandlesticks = subject.getCandlesticks(isin);

        assertTrue(actualCandlesticks.isEmpty());
        verifyNoInteractions(candlestickFactory);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("generateCandlestickTestParams")
    void shouldGenerateCandlesticksCorrectly(String isin,
                                    final List<List<Quote>> candlestickQuotes,
                                    final List<Candlestick> candlesticks,
                                    final List<Candlestick> expectedCandlesticks) {

        List<Quote> quotes = candlestickQuotes.stream()
                .flatMap(Collection::stream)
                .toList();

        when(quoteFinder.fetchQuotes(eq(isin), anyInt()))
                .thenReturn(quotes);

        IntStream.range(0, candlestickQuotes.size())
                .forEach(index -> {
                    var quoteList = candlestickQuotes.get(index);
                    var candlestick = candlesticks.get(index);

                    when(candlestickFactory.generateCandlestick(quoteList))
                            .thenReturn(Optional.of(candlestick));
                });

        var actualCandlesticks = subject.getCandlesticks(isin);

        assertEquals(expectedCandlesticks.size(), actualCandlesticks.size());
        assertIterableEquals(expectedCandlesticks, actualCandlesticks);
    }

    public static Stream<Arguments> generateCandlestickTestParams() {
        var isin = "ABC";

        var candlestickQuotes1 = List.of(
                new Quote(isin, 10, Instant.parse("2023-04-21T12:01:01.00Z")),
                new Quote(isin, 9, Instant.parse("2023-04-21T12:01:04.00Z")),
                new Quote(isin, 12.4, Instant.parse("2023-04-21T12:01:13.00Z")),
                new Quote(isin, 25, Instant.parse("2023-04-21T12:01:49.00Z")),
                new Quote(isin, 23.3, Instant.parse("2023-04-21T12:01:59.00Z"))
        );
        var candlestickQuotes2 = List.of(
                new Quote(isin, 103, Instant.parse("2023-04-21T12:02:01.00Z")),
                new Quote(isin, 124, Instant.parse("2023-04-21T12:02:03.00Z")),
                new Quote(isin, 90, Instant.parse("2023-04-21T12:02:15.00Z")),
                new Quote(isin, 233, Instant.parse("2023-04-21T12:02:29.00Z")),
                new Quote(isin, 212, Instant.parse("2023-04-21T12:02:45.00Z"))
        );
        var candlestickQuotes3 = List.of(
                new Quote(isin, 189, Instant.parse("2023-04-21T12:05:01.00Z")),
                new Quote(isin, 242, Instant.parse("2023-04-21T12:05:03.00Z")),
                new Quote(isin, 160, Instant.parse("2023-04-21T12:05:15.00Z")),
                new Quote(isin, 199, Instant.parse("2023-04-21T12:05:29.00Z")),
                new Quote(isin, 300, Instant.parse("2023-04-21T12:05:45.00Z"))
        );
        var candlestickQuotes4 = List.of(
                new Quote(isin, 189, Instant.parse("2023-04-21T12:07:01.00Z")),
                new Quote(isin, 242, Instant.parse("2023-04-21T12:07:03.00Z")),
                new Quote(isin, 160, Instant.parse("2023-04-21T12:07:15.00Z")),
                new Quote(isin, 199, Instant.parse("2023-04-21T12:07:29.00Z")),
                new Quote(isin, 300, Instant.parse("2023-04-21T12:07:45.00Z"))
        );

        var candlestick1 = Candlestick.builder()
                .openTimestamp(Instant.parse("2023-04-21T12:01:01.00Z"))
                .closeTimestamp(Instant.parse("2023-04-21T12:01:59.00Z"))
                .openPrice(10.0)
                .closingPrice(23.3)
                .highPrice(25.0)
                .lowPrice(9.0)
                .build();

        var candlestick2 = Candlestick.builder()
                .openTimestamp(Instant.parse("2023-04-21T12:02:01.00Z"))
                .closeTimestamp(Instant.parse("2023-04-21T12:02:45.00Z"))
                .openPrice(103.0)
                .closingPrice(212.0)
                .highPrice(233.0)
                .lowPrice(90.0)
                .build();

        var candlestick3 = Candlestick.builder()
                .openTimestamp(Instant.parse("2023-04-21T12:05:01.00Z"))
                .closeTimestamp(Instant.parse("2023-04-21T12:05:45.00Z"))
                .openPrice(189.0)
                .closingPrice(300.)
                .highPrice(300.)
                .lowPrice(160.)
                .build();

        var candlestick4 = Candlestick.builder()
                .openTimestamp(Instant.parse("2023-04-21T12:07:01.00Z"))
                .closeTimestamp(Instant.parse("2023-04-21T12:07:45.00Z"))
                .openPrice(189.0)
                .closingPrice(300.)
                .highPrice(300.)
                .lowPrice(160.)
                .build();

        return Stream.of(
                Arguments.of(
                        Named.of("When there are no time gaps between quotes", isin),
                        List.of(candlestickQuotes2, candlestickQuotes1),
                        List.of(candlestick2, candlestick1),
                        List.of(candlestick1, candlestick2)
                ),
                Arguments.of(
                        Named.of("When there is a gap of 2 minutes between quotes", isin),
                        List.of(candlestickQuotes2, candlestickQuotes1, candlestickQuotes3),
                        List.of(candlestick2, candlestick1, candlestick3),
                        List.of(candlestick1, candlestick2, candlestick2, candlestick2, candlestick3)
                ),
                Arguments.of(
                        Named.of("When there is multiple gaps between quotes", isin),
                        List.of(candlestickQuotes2, candlestickQuotes1, candlestickQuotes3, candlestickQuotes4),
                        List.of(candlestick2, candlestick1, candlestick3, candlestick4),
                        List.of(candlestick1, candlestick2, candlestick2, candlestick2, candlestick3, candlestick3, candlestick4)
                )
        );
    }
}
