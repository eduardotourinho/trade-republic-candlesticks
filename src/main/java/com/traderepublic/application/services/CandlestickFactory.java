package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class CandlestickFactory {

    public Optional<Candlestick> generateCandlestick(List<Quote> quotes) {
        if (quotes.isEmpty()) {
            return Optional.empty();
        }

        var sortedQuotes = quotes.stream()
                .sorted(Comparator.comparing(Quote::timestamp))
                .toList();

        var openQuote = sortedQuotes.get(0);
        var closeQuote = sortedQuotes.get(quotes.size()-1);

        var openTimestamp = LocalDateTime.from(openQuote.timestamp().atOffset(ZoneOffset.UTC))
                .withSecond(0).withNano(0).toInstant(ZoneOffset.UTC);

        var closeTimestamp = openTimestamp.plusSeconds(60);

        var maxPrice = quotes.stream()
                .max(Comparator.comparingDouble(Quote::price))
                .orElseThrow().price();

        var minPrice = quotes.stream()
                .min(Comparator.comparingDouble(Quote::price))
                .orElseThrow().price();

        return Optional.of(Candlestick.builder()
                .openTimestamp(openTimestamp)
                .openPrice(openQuote.price())
                .closeTimestamp(closeTimestamp)
                .closingPrice(closeQuote.price())
                .highPrice(maxPrice)
                .lowPrice(minPrice)
                .build());
    }
}
