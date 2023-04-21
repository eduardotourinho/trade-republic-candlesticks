package com.traderepublic.adapters.out.storage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Quotes")
public class QuoteEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    InstrumentEntity instrument;

    double price;

    @CreatedDate
    Instant timestamp;
}
