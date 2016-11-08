package com.rbs.hackathon.txeventsourcing.model;

public enum TransactionType {
    DIRECT_DEBIT("D/D"), CARD_PAYMENT("POS"), CHEQUE("CHQ"), ATM("C/L"), STANDING_ORDER("S/O"), BACS("BASC");

    private final String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
