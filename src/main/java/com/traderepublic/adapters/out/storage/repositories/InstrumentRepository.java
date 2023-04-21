package com.traderepublic.adapters.out.storage.repositories;

import com.traderepublic.adapters.out.storage.models.InstrumentEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstrumentRepository extends CrudRepository<InstrumentEntity, UUID> {

    Optional<InstrumentEntity> findByIsin(@NonNull String isin);
}
