package com.traderepublic.adapters.out.storage;

import com.traderepublic.adapters.out.storage.models.InstrumentEntity;
import com.traderepublic.adapters.out.storage.models.QuoteEntity;
import com.traderepublic.adapters.out.storage.repositories.InstrumentRepository;
import com.traderepublic.adapters.out.storage.repositories.QuoteRepository;
import com.traderepublic.application.models.Quote;
import com.traderepublic.application.ports.out.InstrumentManagerPort;
import com.traderepublic.application.ports.out.QuoteFinderPort;
import com.traderepublic.application.ports.out.QuoteManagerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentStorageManager implements InstrumentManagerPort, QuoteManagerPort, QuoteFinderPort {

    private final InstrumentRepository instrumentRepository;
    private final QuoteRepository quoteRepository;

    @Override
    @Transactional
    public void addInstrument(String isin, String description) {
        var instrument = instrumentRepository.findByIsin(isin);

        if (instrument.isPresent()) {
            log.error("Instrument {} already exist", isin);
            return;
        }

        var newInstrument = InstrumentEntity.builder()
                .id(UUID.randomUUID())
                .isin(isin)
                .description(description)
                .build();

        instrumentRepository.save(newInstrument);
    }

    @Override
    @Transactional
    public void deleteInstrument(String isin) {
        var instrument = instrumentRepository.findByIsin(isin);

        if (instrument.isEmpty()) {
            log.error("Instrument {} does not exist", isin);
            return;
        }

        instrumentRepository.delete(instrument.get());
    }

    @Override
    @Transactional
    public void saveQuote(String isin, double price, Instant timestamp) {
        var instrument = instrumentRepository.findByIsin(isin);
        if (instrument.isEmpty()) {
            log.error("Couldn't save quote: ISIN {} does not exist", isin);
            return;
        }

        var quoteEntity = QuoteEntity.builder()
                .instrument(instrument.get())
                .price(price)
                .timestamp(timestamp)
                .build();

        quoteRepository.save(quoteEntity);
    }

    @Override
    public List<Quote> fetchQuotes(String isin, Instant initialTimestamp, Instant endTimestamp) {
        var instrument = instrumentRepository.findByIsin(isin);
        if (instrument.isEmpty()) {
            log.error("Instrument {} doesn't exist", isin);
            return List.of();
        }

        var quoteEntities = quoteRepository.findByIsinAndTimestamp(instrument.get().getIsin(), initialTimestamp,
                endTimestamp);

        return quoteEntities.stream()
                .map(e -> new Quote(instrument.get().getIsin(), e.getPrice(), e.getTimestamp()))
                .collect(Collectors.toList());
    }
}
