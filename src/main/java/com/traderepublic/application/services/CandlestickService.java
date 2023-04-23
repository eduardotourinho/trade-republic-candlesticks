package com.traderepublic.application.services;

import com.traderepublic.application.config.CandlestickConfig;
import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import com.traderepublic.application.ports.in.FindCandlesticksUseCase;
import com.traderepublic.application.ports.out.QuoteFinderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandlestickService implements FindCandlesticksUseCase {

    private final CandlestickConfig config;

    private final QuoteFinderPort quoteFinder;
    private final CandlestickFactory candlestickFactory;

    @Override
    public List<Candlestick> getCandlesticks(String isin) {
        var groupedQuotes = groupQuotes(quoteFinder.fetchQuotes(isin, config.getAggregationTimeframeMinutes()));

        return groupedQuotes
                .values().stream()
                .map(candlestickFactory::generateCandlestick)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Map<LocalTime, List<Quote>> groupQuotes(List<Quote> quotes) {
        var groupedQuotes = quotes.stream()
                .sorted(Comparator.comparing(Quote::timestamp))
                .collect(Collectors.groupingBy(
                        quote -> LocalTime.ofInstant(quote.timestamp(), ZoneOffset.UTC).withSecond(0).withNano(0),
                        TreeMap::new,
                        Collectors.toList()
                ));

        return generateMissingQuotes(groupedQuotes);
    }

    private Map<LocalTime, List<Quote>> generateMissingQuotes(final Map<LocalTime, List<Quote>> quotes) {
        if (quotes.isEmpty()) {
            return quotes;
        }

        var minTime = quotes.keySet().stream().min(LocalTime::compareTo).get();
        var maxTime = quotes.keySet().stream().max(LocalTime::compareTo).get();
        var diffMinutes = minTime.until(maxTime, ChronoUnit.MINUTES);

        for (var minute = 1; minute <= diffMinutes; minute++) {
            var testTime = LocalTime.from(minTime).plusMinutes(minute);

            if (quotes.containsKey(testTime)) {
                continue;
            }

            var previousQuotes = findPastQuotes(quotes, testTime, minTime);
            quotes.put(testTime, previousQuotes);
        }

        return quotes;
    }

    private List<Quote> findPastQuotes(final Map<LocalTime, List<Quote>> quotes,
                                       final LocalTime time, final LocalTime firstTime) {
        if (time.isBefore(firstTime)) {
            return quotes.get(firstTime);
        }

        var previousTime = LocalTime.from(time).minusMinutes(1);
        if (!quotes.containsKey(previousTime)) {
            return findPastQuotes(quotes, previousTime, firstTime);
        }

        return quotes.get(previousTime);
    }
}
