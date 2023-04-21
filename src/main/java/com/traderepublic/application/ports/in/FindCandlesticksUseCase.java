package com.traderepublic.application.ports.in;

import com.traderepublic.application.models.Candlestick;

import java.util.List;

public interface FindCandlesticksUseCase {

    List<Candlestick> getCandlesticks(String isin);
}
