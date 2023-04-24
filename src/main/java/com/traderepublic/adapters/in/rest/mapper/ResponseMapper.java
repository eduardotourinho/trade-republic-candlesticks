package com.traderepublic.adapters.in.rest.mapper;

import com.traderepublic.adapters.in.rest.models.CandlestickResponse;
import com.traderepublic.application.models.Candlestick;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Component
public class ResponseMapper {

    public CandlestickResponse.Candlestick responseFrom(Candlestick candlestick) {
        var openTimestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var closeTimestampFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        var openDateTime = LocalDateTime.from(candlestick.getOpenTimestamp().atOffset(ZoneOffset.UTC))
                .format(openTimestampFormat);
        var closeDateTime = LocalDateTime.from(candlestick.getCloseTimestamp().atOffset(ZoneOffset.UTC))
                .format(closeTimestampFormat);

        return CandlestickResponse.Candlestick.builder()
                .openTimestamp(openDateTime)
                .closeTimestamp(closeDateTime)
                .openPrice(roundValue(candlestick.getOpenPrice()))
                .closingPrice(roundValue(candlestick.getClosingPrice()))
                .highPrice(roundValue(candlestick.getHighPrice()))
                .lowPrice(roundValue(candlestick.getLowPrice()))
                .build();
    }

    private double roundValue(double value) {
        return (double) Math.round(value * 100) / 100;
    }
}
