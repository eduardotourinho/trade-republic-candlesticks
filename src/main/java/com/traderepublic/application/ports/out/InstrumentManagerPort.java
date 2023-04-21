package com.traderepublic.application.ports.out;

public interface InstrumentManagerPort {

    void addInstrument(String isin, String description);

    void deleteInstrument(String isin);
}
