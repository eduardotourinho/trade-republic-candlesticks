package com.traderepublic.adapters.in.rest.models;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandlestickResponse {

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
