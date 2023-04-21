package com.traderepublic.adapters.out.storage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Instruments")
public class InstrumentEntity {

    @Id
    UUID id;

    String isin;

    String description;

    @OneToMany(mappedBy = "instrument", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<QuoteEntity> quotes;
}
