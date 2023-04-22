package com.traderepublic.application.services;

import com.traderepublic.application.ports.out.InstrumentManagerPort;
import com.traderepublic.application.ports.out.QuoteManagerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstrumentManagerTest {

    @Mock
    private InstrumentManagerPort instrumentPort;
    @Mock
    private QuoteManagerPort quotePort;

    @InjectMocks
    private InstrumentManager subject;

    @Test
    void shouldCallDeleteInstrumentOnInstrumentManagerPort() {
        subject.deleteInstrument("ABC");

        verify(instrumentPort, times(1))
                .deleteInstrument("ABC");
        verifyNoMoreInteractions(instrumentPort);
        verifyNoInteractions(quotePort);
    }

    @Test
    void shouldCallAddInstrumentOnInstrumentManagerPort() {
        subject.addInstrument("ABC", "Test instrument");

        verify(instrumentPort, times(1))
                .addInstrument("ABC", "Test instrument");
        verifyNoMoreInteractions(instrumentPort);
        verifyNoInteractions(quotePort);
    }

    @Test
    void shouldCallSaveQuoteOnQuoteManagerPort() {
        subject.saveQuote("ABC", 12.4);

        verify(quotePort, times(1))
                .saveQuote(eq("ABC"), eq(12.4), any(Instant.class));
        verifyNoMoreInteractions(quotePort);
        verifyNoInteractions(instrumentPort);
    }
}
