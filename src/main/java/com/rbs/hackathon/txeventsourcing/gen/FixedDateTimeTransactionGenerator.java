package com.rbs.hackathon.txeventsourcing.gen;

import com.rbs.hackathon.txeventsourcing.TxGenerator;
import com.rbs.hackathon.txeventsourcing.model.TimeUnit;
import com.rbs.hackathon.txeventsourcing.model.TransactionModel;
import com.rbs.hackathon.txeventsourcing.model.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FixedDateTimeTransactionGenerator implements TxGenerator {
    private String accountNumber;
    private String merchant;
    private List<String> randomMerchants;
    private String category;
    private TimeUnit occurrenceTimeUnit;
    private int dayOfTheMonth;
    private int hourOfDay = 9;
    private int dayOfWeek;
    private TransactionType transactionType;

    private AmountGenerator amountGenerator;

    @Override
    public List<TransactionModel> generate(ZonedDateTime from, ZonedDateTime to) {

        List<TransactionModel> result = new ArrayList<>();

        ZonedDateTime start = from;
        while (start.isBefore(to)) {
            if (trigger(start)) {
                //generate
                TransactionModel tx = new TransactionModel()
                        .setTimestamp(start.plusSeconds(RandomUtils.nextInt(360)))
                        .setAccountId(accountNumber)
                        .setMerchant(StringUtils.isNotBlank(merchant) ? merchant : randomMerchants.get(RandomUtils.nextInt(randomMerchants.size())))
                        .setAmount(amountGenerator.generate())
                        .setCategory(category)
                        .setId(UUID.randomUUID().toString())
                        .setType(transactionType);
                result.add(tx);


            }
            start = start.plusHours(1);
        }
        return result;
    }

    public boolean trigger(ZonedDateTime dateTime) {
        switch (occurrenceTimeUnit) {
            case MONTHLY:
                return dateTime.getDayOfMonth() == this.dayOfTheMonth && dateTime.getHour() == hourOfDay;
            case WEEKLY:
                return dateTime.getDayOfWeek().getValue() == this.dayOfWeek && dateTime.getHour() == hourOfDay;
            case DAILY:
                return dateTime.getHour() == hourOfDay;
            default:
                throw new RuntimeException("Invalid:" + occurrenceTimeUnit);
        }

    }


}
