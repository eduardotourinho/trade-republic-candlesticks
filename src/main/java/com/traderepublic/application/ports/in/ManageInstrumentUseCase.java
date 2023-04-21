package com.traderepublic.application.ports.in;

public interface ManageInstrumentUseCase {

    void addInstrument(String isin, String description);

    void deleteInstrument(String isin);
}
