package com.traderepublic.adapters.out.storage.repositories;

import com.traderepublic.adapters.out.storage.models.QuoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuoteRepository extends CrudRepository<QuoteEntity, UUID> {

    @Query(value = "SELECT q FROM QuoteEntity q WHERE q.instrument.isin = ?1 "
            + "AND q.timestamp >= ?2 AND q.timestamp < ?3")
    List<QuoteEntity> findByIsinAndTimestamp(String isin, Instant startPeriod, Instant endPeriod);
}
