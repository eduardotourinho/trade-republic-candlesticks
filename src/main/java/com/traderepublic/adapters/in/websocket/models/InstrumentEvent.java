package com.traderepublic.adapters.in.websocket.models;

public record InstrumentEvent(InstrumentType type, Data data) {
    public enum InstrumentType {ADD, DELETE}

    public record Data (String isin, String description) {}
}
