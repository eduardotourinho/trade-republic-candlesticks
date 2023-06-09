package com.traderepublic.adapters.in.websocket;

import com.traderepublic.adapters.in.websocket.config.PartnerConfig;
import com.traderepublic.adapters.in.websocket.handlers.InstrumentStreamHandler;
import com.traderepublic.adapters.in.websocket.handlers.QuotesStreamHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerStreamsClient {

    private final PartnerConfig partnerConfig;

    private final InstrumentStreamHandler instrumentsHandler;
    private final QuotesStreamHandler quotesStreamHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void listenWebSockets() {
        if (!partnerConfig.isEnabled()) {
            log.info("Partner connection is not enabled in the application");
            return;
        }

        log.info("Connecting to partner server...");

        var webSocketClient = new StandardWebSocketClient();
        webSocketClient.execute(instrumentsHandler, partnerConfig.getInstrumentUri());
        webSocketClient.execute(quotesStreamHandler, partnerConfig.getQuotesUri());
    }
}
