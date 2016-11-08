package com.rbs.hackathon.txeventsourcing.model;

import com.rbs.hackathon.txeventsourcing.api.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TransactionModel {
    private String id;
    private String accountId;
    private ZonedDateTime timestamp;
    private double amount;
    private TransactionType type;
    private String merchant;
    private String category;

    public Transaction toApi() {
        return new Transaction()
                .setId(id)
                .setAccountId(accountId)
                .setTransactionDateTime(timestamp.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .setTransactionAmount(amount)
                .setType(type.getCode())
                .setTransactionDescription(merchant)
                .setCategory(category)
                .setUnfamiliar(false);
    }
}
