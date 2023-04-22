package com.traderepublic.adapters.in.rest;

import com.traderepublic.adapters.in.rest.models.CandlestickResponse;
import com.traderepublic.application.ports.in.ManageInstrumentUseCase;
import com.traderepublic.application.ports.in.ManageQuoteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CandlestickControllerIntegrationTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private ManageInstrumentUseCase instrumentUseCase;

    @Autowired
    private ManageQuoteUseCase quoteUseCase;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        instrumentUseCase.addInstrument("ABC", "Test instrument");
        quoteUseCase.saveQuote("ABC", 120.03);
        quoteUseCase.saveQuote("ABC", 220.03);
        quoteUseCase.saveQuote("ABC", 250.03);
    }

    @Test
    public void shouldReturnCandlestick() {
        var response = restTemplate.getForEntity(url("/candlesticks?isin=ABC"), CandlestickResponse.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCandlesticks().size());
    }

    private String url(String path) {
        return String.format("http://localhost:%d/%s", port, path);
    }
}