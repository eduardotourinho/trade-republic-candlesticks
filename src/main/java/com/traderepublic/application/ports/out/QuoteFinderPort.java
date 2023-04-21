package com.traderepublic.application.ports.out;

import com.traderepublic.application.models.Quote;

import java.util.List;

public interface QuoteFinderPort {

    List<Quote> fetchQuotes(String isin, int pastMinutes);
}
