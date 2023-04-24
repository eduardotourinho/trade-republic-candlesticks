package com.traderepublic.adapters.in.rest;

import com.traderepublic.adapters.in.rest.models.CandlestickResponse;
import com.traderepublic.application.models.Candlestick;
import com.traderepublic.application.ports.in.FindCandlesticksUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CandlestickControllerIntegrationTest {

    @Value(value = "${local.server.port}")
    private int port;

    @MockBean
    private FindCandlesticksUseCase aggregateCandlesticksMock;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        var candlesticks = List.of(
                Candlestick.builder()
                        .openTimestamp(Instant.parse("2023-04-21T13:04:00.00Z"))
                        .openPrice(12.0)
                        .closeTimestamp(Instant.parse("2023-04-21T13:05:00.00Z"))
                        .closingPrice(30.0)
                        .highPrice(32.1)
                        .lowPrice(9.0)
                        .build(),
                Candlestick.builder()
                        .openTimestamp(Instant.parse("2023-04-21T13:03:00.00Z"))
                        .openPrice(12.0)
                        .closeTimestamp(Instant.parse("2023-04-21T13:04:00.00Z"))
                        .closingPrice(30.0)
                        .highPrice(32.1)
                        .lowPrice(9.0)
                        .build()
        );

        when(aggregateCandlesticksMock.getCandlesticks("ABC"))
                .thenReturn(candlesticks);
    }

    @Test
    public void shouldReturnCandlestickResponse() {
        final var expectedCandlestickResponse = List.of(
                CandlestickResponse.Candlestick.builder()
                        .openTimestamp("2023-04-21 13:04:00")
                        .openPrice(12.0)
                        .closeTimestamp("13:05:00")
                        .closingPrice(30.0)
                        .highPrice(32.1)
                        .lowPrice(9.0)
                        .build(),
                CandlestickResponse.Candlestick.builder()
                        .openTimestamp("2023-04-21 13:03:00")
                        .openPrice(12.0)
                        .closeTimestamp("13:04:00")
                        .closingPrice(30.0)
                        .highPrice(32.1)
                        .lowPrice(9.0)
                        .build()
        );

        var response = restTemplate.getForEntity(url("/candlesticks?isin=ABC"), CandlestickResponse.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCandlesticks().size());
        assertIterableEquals(expectedCandlestickResponse, response.getBody().getCandlesticks());
    }

    private String url(String path) {
        return String.format("http://localhost:%d/%s", port, path);
    }
}
