package com.traderepublic.application.models;

import java.time.Instant;

public record Quote(String isin, double price, Instant timestamp) {
}
