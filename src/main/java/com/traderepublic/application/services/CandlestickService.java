package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import com.traderepublic.application.ports.in.FindCandlesticksUseCase;
import com.traderepublic.application.ports.out.QuoteFinderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandlestickService implements FindCandlesticksUseCase {

    private static final int DEFAULT_AGGREGATION_MINUTES = 30;

    private final QuoteFinderPort quoteFinder;
    private final CandlestickFactory candlestickFactory;

    @Override
    @Cacheable("candlesticks")
    public List<Candlestick> getCandlesticks(String isin) {
        var groupedQuotes = groupQuotes(quoteFinder.fetchQuotes(isin, DEFAULT_AGGREGATION_MINUTES));

        return groupedQuotes
                .values().stream()
                .map(candlestickFactory::generateCandlestick)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Map<String, List<Quote>> groupQuotes(List<Quote> quotes) {
        var groupedQuotes = quotes.stream()
                .sorted(Comparator.comparing(Quote::timestamp))
                .collect(Collectors.groupingBy(
                        quote -> {
                            var localtime = LocalTime.ofInstant(quote.timestamp(), ZoneOffset.UTC);
                            return localtime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00"));
                        },
                        Collectors.toList()
                ));

        return generateMissingTimestamps(groupedQuotes);
    }

    private Map<String, List<Quote>> generateMissingTimestamps(Map<String, List<Quote>> quotes) {
        // TODO: Verify if all the minutes between the min(timestamp) and max(timestamp) are present.
        // If not, create a new entry and use the previous values
        var dates = quotes.keySet().stream()
                .map(LocalDateTime::parse)
                .peek(datetime -> log.debug(datetime.toString()))
                .toList();


        return quotes;
    }
}
