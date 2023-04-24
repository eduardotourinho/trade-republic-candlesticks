package com.traderepublic.adapters.out.storage;

import com.traderepublic.adapters.out.storage.models.InstrumentEntity;
import com.traderepublic.adapters.out.storage.models.QuoteEntity;
import com.traderepublic.adapters.out.storage.repositories.InstrumentRepository;
import com.traderepublic.adapters.out.storage.repositories.QuoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("integration-test")
@SpringBootTest
class InstrumentStorageManagerIntegrationTest {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private InstrumentStorageManager subject;

    @AfterEach
    public void cleanUp() {
        instrumentRepository.deleteAll();
        quoteRepository.deleteAll();
    }

    @Test
    void shouldSaveInstrumentToDb() {
        subject.addInstrument("ABC", "Test instrument");

        var actualEntity = instrumentRepository.findByIsin("ABC");

        assertTrue(actualEntity.isPresent());
        assertEquals("ABC", actualEntity.get().getIsin());
        assertEquals("Test instrument", actualEntity.get().getDescription());
    }

    @Test
    void shouldDeleteInstrumentFromDb() {
        subject.addInstrument("ABC", "Test instrument");

        var savedEntity = instrumentRepository.findByIsin("ABC");
        assertTrue(savedEntity.isPresent());

        subject.deleteInstrument("ABC");
        var deletedEntity = instrumentRepository.findByIsin("ABC");

        assertTrue(deletedEntity.isEmpty());
    }

    @Test
    void shouldDeleteInstrumentAndAllTheQuotesFromDB() {
        subject.addInstrument("ABC", "Test instrument");

        var savedEntity = instrumentRepository.findByIsin("ABC");
        assertTrue(savedEntity.isPresent());

        quoteRepository.saveAll(List.of(
                QuoteEntity.builder()
                        .timestamp(Instant.now().minus(1, ChronoUnit.MINUTES))
                        .price(32.0)
                        .instrument(savedEntity.get())
                        .build(),
                QuoteEntity.builder()
                        .instrument(savedEntity.get())
                        .price(26.4)
                        .timestamp(Instant.now())
                        .build()
        ));

        var savedQuotes = (List<QuoteEntity>) quoteRepository.findAll();
        assertEquals(2, savedQuotes.size());

        subject.deleteInstrument("ABC");

        var deletedEntity = instrumentRepository.findByIsin("ABC");
        var deletedQuotes = (List<QuoteEntity>) quoteRepository.findAll();

        assertTrue(deletedEntity.isEmpty());
        assertTrue(deletedQuotes.isEmpty());
    }

    @Test
    void shouldLogErrorWhenDeleteInstrumentNotExist(CapturedOutput output) {
        subject.deleteInstrument("ABC");

        assertTrue(output.getOut().contains("Instrument ABC does not exist"));
    }

    @Test
    void shouldAddQuotesToDb() {
        subject.addInstrument("ABC", "Test instrument");

        var savedEntity = instrumentRepository.findByIsin("ABC");
        assertTrue(savedEntity.isPresent());

        subject.saveQuote("ABC", 40.2, Instant.now());
        var actualQuotes = (List<QuoteEntity>) quoteRepository.findAll();

        assertEquals(1, actualQuotes.size());
    }

    @Test
    void shouldLogErrorWhenAddQuotesToNonExistentInstrument(CapturedOutput output) {
        subject.saveQuote("ABC", 40.2, Instant.now());

        assertTrue(output.getOut().contains("Couldn't save quote: ISIN ABC does not exist"));
    }

    @Test
    void shouldFindAllQuotesFromAPeriod() {
        // Prepare
        var startTimestamp = Instant.parse("2023-04-23T13:30:00.00Z");
        var endTimestamp = Instant.parse("2023-04-23T14:00:00.00Z");

        subject.addInstrument("ABC", "Test instrument");

        var instrumentEntity = instrumentRepository.findByIsin("ABC");
        assertTrue(instrumentEntity.isPresent());

        var quotesToSave = getQuoteEntityList(instrumentEntity.get());
        quoteRepository.saveAll(quotesToSave);

        var allQuotes = (List<QuoteEntity>) quoteRepository.findAll();
        assertEquals(quotesToSave.size(), allQuotes.size());

        // Act
        var actualQuotes = subject.fetchQuotes("ABC", startTimestamp, endTimestamp);

        // Assert
        assertEquals(4, actualQuotes.size());
    }

    @Test
    void shouldLogErrorIfInstrumentNotFoundWhenFindingQuotes(CapturedOutput output) {
        var startTimestamp = Instant.parse("2023-04-23T13:30:00.00Z");
        var endTimestamp = Instant.parse("2023-04-23T14:00:00.00Z");

        subject.addInstrument("ABC", "Test instrument");

        var instrumentEntity = instrumentRepository.findByIsin("ABC");
        assertTrue(instrumentEntity.isPresent());

        var quotesToSave = getQuoteEntityList(instrumentEntity.get());
        quoteRepository.saveAll(quotesToSave);

        var actualQuotes = subject.fetchQuotes("DEF", startTimestamp, endTimestamp);

        assertTrue(actualQuotes.isEmpty());
        assertTrue(output.getOut().contains("Instrument DEF doesn't exist"));
    }

    private List<QuoteEntity> getQuoteEntityList(InstrumentEntity instrumentEntity) {
        return List.of(
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(70.2)
                        .timestamp(Instant.parse("2023-04-23T13:29:59.00Z"))
                        .build(),
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(60.2)
                        .timestamp(Instant.parse("2023-04-23T13:30:00.00Z"))
                        .build(),
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(50.2)
                        .timestamp(Instant.parse("2023-04-23T13:32:24.00Z"))
                        .build(),
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(40.2)
                        .timestamp(Instant.parse("2023-04-23T13:45:48.00Z"))
                        .build(),
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(30.2)
                        .timestamp(Instant.parse("2023-04-23T13:59:59.00Z"))
                        .build(),
                QuoteEntity.builder()
                        .instrument(instrumentEntity)
                        .price(30.2)
                        .timestamp(Instant.parse("2023-04-23T14:00:00.00Z"))
                        .build()
        );
    }
}
