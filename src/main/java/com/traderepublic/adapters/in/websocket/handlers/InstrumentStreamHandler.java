package com.traderepublic.adapters.in.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderepublic.adapters.in.websocket.models.InstrumentEvent;
import com.traderepublic.application.ports.in.ManageInstrumentUseCase;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstrumentStreamHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ManageInstrumentUseCase instrumentManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected to instruments stream");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, @Nonnull TextMessage message) throws Exception {
        var instrumentEvent = objectMapper.readValue(message.getPayload(), InstrumentEvent.class);
        log.debug("InstrumentEvent: {}", instrumentEvent);

        if (instrumentEvent.type() == InstrumentEvent.InstrumentType.ADD) {
            instrumentManager.addInstrument(instrumentEvent.data().isin(), instrumentEvent.data().description());
        } else if (instrumentEvent.type() == InstrumentEvent.InstrumentType.DELETE) {
            instrumentManager.deleteInstrument(instrumentEvent.data().isin());
        } else {
            log.error("Unknown event type");
        }
    }
}
