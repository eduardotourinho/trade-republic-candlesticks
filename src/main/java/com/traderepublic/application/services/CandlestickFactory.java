package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import org.springframework.stereotype.Component;

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

        var maxPrice = quotes.stream()
                .max(Comparator.comparingDouble(Quote::price))
                .get().price();

        var minPrice = quotes.stream()
                .min(Comparator.comparingDouble(Quote::price))
                .get().price();

        return Optional.of(Candlestick.builder()
                .openTimestamp(openQuote.timestamp())
                .openPrice(openQuote.price())
                .closeTimestamp(closeQuote.timestamp())
                .closingPrice(closeQuote.price())
                .highPrice(maxPrice)
                .lowPrice(minPrice)
                .build());
    }
}
