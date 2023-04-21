package com.traderepublic.adapters.in.rest;

import com.traderepublic.adapters.in.rest.models.CandleStickResponse;
import com.traderepublic.application.ports.in.FindCandlesticksUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CandlestickController {

    private final FindCandlesticksUseCase aggregateCandlesticks;

    @GetMapping("/candlesticks")
    @ResponseBody
    public CandleStickResponse getCandlesticks(@RequestParam @NonNull String isin) {
        var candlesticks = aggregateCandlesticks.getCandlesticks(isin);

        var candlestickList = candlesticks.stream()
                .map(candlestick -> CandleStickResponse.Candlestick.builder()
                        .openTimestamp(candlestick.getOpenTimestamp())
                        .closeTimestamp(candlestick.getCloseTimestamp())
                        .openPrice(candlestick.getOpenPrice())
                        .closingPrice(candlestick.getClosingPrice())
                        .highPrice(candlestick.getHighPrice())
                        .lowPrice(candlestick.getLowPrice())
                        .build())
                .toList();

        return CandleStickResponse.builder()
                .candlesticks(candlestickList)
                .build();
    }
}
