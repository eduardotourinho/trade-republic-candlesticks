package com.traderepublic.adapters.in.websocket.models;

public record QuoteEvent(Data data) {

    public record Data(String isin, Double price) {}
}
