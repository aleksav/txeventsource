package com.rbs.hackathon.txeventsourcing.gen;

import com.rbs.hackathon.txeventsourcing.TxGenerator;
import com.rbs.hackathon.txeventsourcing.model.TimeUnit;
import com.rbs.hackathon.txeventsourcing.model.TransactionModel;
import com.rbs.hackathon.txeventsourcing.model.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.RandomUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RandomTransactionGenerator implements TxGenerator {
    private String accountNumber;
    private String merchant;
    private List<String> randomMerchants;
    private String category;
    private int maxOccurrences;
    private int minOccurrences;
    private TimeUnit occurrenceTimeUnit;
    private int sessionOccurrences;
    private TransactionType transactionType;
    private IntRange allowedHours = new IntRange(9, 22);
    private AmountGenerator amountGenerator;


    public RandomTransactionGenerator monthly(int min, int max) {
        this.minOccurrences = min;
        this.maxOccurrences = max;
        this.occurrenceTimeUnit = TimeUnit.MONTHLY;
        return this;
    }

    public RandomTransactionGenerator weekly(int min, int max) {
        this.minOccurrences = min;
        this.maxOccurrences = max;
        this.occurrenceTimeUnit = TimeUnit.WEEKLY;
        return this;
    }

    @Override
    public List<TransactionModel> generate(ZonedDateTime from, ZonedDateTime to) {

        List<TransactionModel> result = new ArrayList<>();

        ZonedDateTime start = from;
        while (start.isBefore(to)) {
            if (trigger(start)) {
                //generate
                for (int i = 0; i < RandomUtils.nextInt(1 + sessionOccurrences); i++) {
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


            }
            start = start.plusHours(1);
        }
        return result;
    }

    public boolean trigger(ZonedDateTime dateTime) {
        if (dateTime.getHour() < allowedHours.getMinimumInteger() || dateTime.getHour() > allowedHours.getMaximumInteger()) {
            return false;
        }

        int range;
        int maxHours = allowedHours.getMaximumInteger() - allowedHours.getMinimumInteger();

        switch (occurrenceTimeUnit) {
            case MONTHLY:
                range = 30 * maxHours;
                break;
            case WEEKLY:
                range = 7 * maxHours;
                break;
            case DAILY:
                range = maxHours;
                break;
            default:
                throw new RuntimeException("Invalid:" + occurrenceTimeUnit);
        }
        int random = RandomUtils.nextInt(range);
        return random >= minOccurrences && random < maxOccurrences;
    }


}
