package com.traderepublic.adapters.in.rest.models;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class CandleStickResponse {

    List<Candlestick> candlesticks;

    @Value
    @Builder
    public static class Candlestick {

        Instant openTimestamp;
        Instant closeTimestamp;
        Double openPrice;
        Double closingPrice;
        Double highPrice;
        Double lowPrice;
    }
}
