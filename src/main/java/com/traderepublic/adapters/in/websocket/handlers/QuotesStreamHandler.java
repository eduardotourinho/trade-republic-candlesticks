package com.traderepublic.adapters.in.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderepublic.adapters.in.websocket.models.QuoteEvent;
import com.traderepublic.application.ports.in.ManageQuoteUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuotesStreamHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ManageQuoteUseCase quoteManager;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        log.info("Connected to quotes stream");
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        var quoteEvent = objectMapper.readValue(message.getPayload(), QuoteEvent.class);
        log.debug("QuoteEvent: {}", quoteEvent);

        quoteManager.saveQuote(quoteEvent.data().isin(), quoteEvent.data().price());
    }
}
