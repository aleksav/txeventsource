package com.rbs.hackathon.txeventsourcing;

import com.rbs.hackathon.txeventsourcing.model.TransactionModel;

import java.time.ZonedDateTime;
import java.util.List;

public interface TxGenerator {
    List<TransactionModel> generate(ZonedDateTime from, ZonedDateTime to);
}
