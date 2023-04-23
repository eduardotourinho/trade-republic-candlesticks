package com.traderepublic.adapters.in.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "partner")
public class PartnerConfig {

    private String instrumentUri;
    private String quotesUri;
    private boolean enabled;
}
