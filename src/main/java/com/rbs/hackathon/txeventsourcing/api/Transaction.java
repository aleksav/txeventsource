package com.rbs.hackathon.txeventsourcing.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Transaction {
    private String id;
    private String accountId;
    private String transactionDateTime;
    private double transactionAmount;
    private double accountBalance;
    private String type;
    private String transactionDescription;
    private String category;
    private boolean unfamiliar;
}
