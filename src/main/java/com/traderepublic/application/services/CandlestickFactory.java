package com.traderepublic.application.services;

import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.models.Quote;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CandlestickFactory {

    public Optional<Candlestick> generateCandlestick(List<Quote> sortedQuotes) {
        if (sortedQuotes.isEmpty()) {
            return Optional.empty();
        }

        var openQuote = sortedQuotes.get(0);
        var closeQuote = sortedQuotes.get(sortedQuotes.size()-1);

        var maxPrice = sortedQuotes.stream()
                .max(Comparator.comparingDouble(Quote::price))
                .get().price();

        var minPrice = sortedQuotes.stream()
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
