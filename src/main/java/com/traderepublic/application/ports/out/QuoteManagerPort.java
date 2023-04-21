package com.traderepublic.application.ports.out;

import java.time.Instant;

public interface QuoteManagerPort {

    void saveQuote(String isin, double price, Instant timestamp);
}
