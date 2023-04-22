package com.traderepublic.adapters.out.storage.repositories;

import com.traderepublic.adapters.out.storage.models.InstrumentEntity;
import com.traderepublic.adapters.out.storage.models.QuoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuoteRepository extends CrudRepository<QuoteEntity, UUID> {

    List<QuoteEntity> findAllByInstrumentAndTimestampBetween(InstrumentEntity instrument, Instant startPeriod, Instant endPeriod);
}
