package com.traderepublic.application.services;

import com.traderepublic.application.ports.in.ManageInstrumentUseCase;
import com.traderepublic.application.ports.in.ManageQuoteUseCase;
import com.traderepublic.application.ports.out.InstrumentManagerPort;
import com.traderepublic.application.ports.out.QuoteManagerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InstrumentManager implements ManageQuoteUseCase, ManageInstrumentUseCase {

    private final InstrumentManagerPort instrumentPort;
    private final QuoteManagerPort quotePort;

    @Override
    public void addInstrument(String isin, String description) {
        instrumentPort.addInstrument(isin, description);
    }

    @Override
    public void deleteInstrument(String isin) {
        instrumentPort.deleteInstrument(isin);
    }

    @Override
    public void saveQuote(String isin, double price) {
        quotePort.saveQuote(isin, price, Instant.now(Clock.systemUTC()));
    }
}
