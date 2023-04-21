package com.traderepublic.application.models;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Candlestick {

    Instant openTimestamp;
    Instant closeTimestamp;
    Double openPrice;
    Double closingPrice;
    Double highPrice;
    Double lowPrice;
}
