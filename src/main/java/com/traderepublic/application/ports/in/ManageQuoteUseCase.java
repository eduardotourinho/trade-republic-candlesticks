package com.traderepublic.application.ports.in;

import java.time.Instant;

public interface ManageQuoteUseCase {

    void saveQuote(String isin, double price);
}
