package com.rbs.hackathon.txeventsourcing;

import com.rbs.hackathon.txeventsourcing.gen.AmountGenerator;
import com.rbs.hackathon.txeventsourcing.gen.FixedDateTimeTransactionGenerator;
import com.rbs.hackathon.txeventsourcing.gen.RandomTransactionGenerator;
import com.rbs.hackathon.txeventsourcing.model.TimeUnit;
import com.rbs.hackathon.txeventsourcing.model.TransactionModel;
import com.rbs.hackathon.txeventsourcing.model.TransactionType;
import org.apache.commons.lang.math.IntRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.*;

public class TxGeneratorRunner {

    private static Logger LOG = LoggerFactory.getLogger(TxGeneratorRunner.class.getSimpleName());


    public static void main(String[] args) {
        ZonedDateTime start = ZonedDateTime.now().minusMonths(6);
        ZonedDateTime end = ZonedDateTime.now();

        new TxGeneratorRunner().generateAll("124332245", start, end);
    }

    public List<TransactionModel> generateAll(String accountNumber, ZonedDateTime start, ZonedDateTime end) {
        RandomTransactionGenerator pub = new RandomTransactionGenerator()
                .monthly(1, 4)
                .setAmountGenerator(AmountGenerator.random(5.0, 15.0))
                .setSessionOccurrences(5)
                .setAccountNumber(accountNumber)
                .setCategory("food-drink")
                .setMerchant("Charles Dickens, Southwark")
                .setAllowedHours(new IntRange(14, 23))
                .setTransactionType(TransactionType.CARD_PAYMENT);

        RandomTransactionGenerator coffee = new RandomTransactionGenerator()
                .weekly(1, 5)
                .setAmountGenerator(AmountGenerator.random(1.0, 5.0))
                .setSessionOccurrences(1)
                .setAccountNumber(accountNumber)
                .setCategory("food-drink")
                .setMerchant("Starbucks")
                .setTransactionType(TransactionType.CARD_PAYMENT);

        RandomTransactionGenerator groceries = new RandomTransactionGenerator()
                .monthly(4, 8)
                .setAmountGenerator(AmountGenerator.random(10.0, 100.0))
                .setSessionOccurrences(1)
                .setAccountNumber(accountNumber)
                .setCategory("grocery")
                .setRandomMerchants(Arrays.asList("Tesco Greenwich", "Waitrose Canary Wharf"))
                .setTransactionType(TransactionType.CARD_PAYMENT);

        RandomTransactionGenerator clothing = new RandomTransactionGenerator()
                .monthly(1, 4)
                .setAmountGenerator(AmountGenerator.random(10.0, 100.0))
                .setSessionOccurrences(1)
                .setAccountNumber(accountNumber)
                .setCategory("clothing")
                .setRandomMerchants(Arrays.asList("H&M Oxford Street London", "Burberry, Regent Street"))
                .setTransactionType(TransactionType.CARD_PAYMENT);

        FixedDateTimeTransactionGenerator mortgage = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(2)
                .setAccountNumber(accountNumber)
                .setCategory("mortgage payment")
                .setMerchant("Natwest")
                .setAmountGenerator(AmountGenerator.fixed(850.00))
                .setTransactionType(TransactionType.DIRECT_DEBIT);

        FixedDateTimeTransactionGenerator gym = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(14)
                .setAccountNumber(accountNumber)
                .setCategory("gym-fitness")
                .setMerchant("Virgin Active")
                .setAmountGenerator(AmountGenerator.fixed(50.00))
                .setTransactionType(TransactionType.STANDING_ORDER);

        FixedDateTimeTransactionGenerator magazine = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(3)
                .setAccountNumber(accountNumber)
                .setCategory("newspapers")
                .setMerchant("Guardian/Observer")
                .setAmountGenerator(AmountGenerator.fixed(30.00))
                .setTransactionType(TransactionType.STANDING_ORDER);

        FixedDateTimeTransactionGenerator mobile = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(6)
                .setAccountNumber(accountNumber)
                .setCategory("telecom")
                .setMerchant("O2 Telefonica")
                .setAmountGenerator(AmountGenerator.random(30.0, 45.0))
                .setTransactionType(TransactionType.DIRECT_DEBIT);

        FixedDateTimeTransactionGenerator salary = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(6)
                .setAccountNumber(accountNumber)
                .setCategory("salary")
                .setMerchant("RBS GROUP")
                .setAmountGenerator(AmountGenerator.random(1500.00, 1600.00).setIncoming(true))
                .setTransactionType(TransactionType.BACS);


        RandomTransactionGenerator atmWithdrawals = new RandomTransactionGenerator()
                .weekly(1, 2)
                .setAmountGenerator(AmountGenerator.discrete(10.00, 20.0, 30.0, 50.0))
                .setSessionOccurrences(1)
                .setAccountNumber(accountNumber)
                .setCategory("cash")
                .setRandomMerchants(Arrays.asList("RBS ATM, Edinburgh Gogab", "LLOYDS ATM, Edinburgh Waverly", "SANTANDER ATM, Bishopsgate"))
                .setTransactionType(TransactionType.ATM);

        RandomTransactionGenerator paymentsToFamilyFriends = new RandomTransactionGenerator()
                .monthly(0, 2)
                .setAmountGenerator(AmountGenerator.discrete(10.00, 20.0, 30.0, 50.0, 100.0))
                .setSessionOccurrences(1)
                .setAccountNumber(accountNumber)
                .setCategory("direct payment")
                .setRandomMerchants(Arrays.asList("David", "Andy", "Phil"))
                .setTransactionType(TransactionType.BACS);

        FixedDateTimeTransactionGenerator utilities = new FixedDateTimeTransactionGenerator()
                .setOccurrenceTimeUnit(TimeUnit.MONTHLY)
                .setDayOfTheMonth(3)
                .setAccountNumber(accountNumber)
                .setCategory("utilities")
                .setMerchant("British Gas")
                .setAmountGenerator(AmountGenerator.random(50.00, 100.00).setIncoming(true))
                .setTransactionType(TransactionType.DIRECT_DEBIT);


        List<TransactionModel> result = new ArrayList<>();
        result.addAll(pub.generate(start, end));
        result.addAll(coffee.generate(start, end));
        result.addAll(mortgage.generate(start, end));
        result.addAll(mobile.generate(start, end));
        result.addAll(atmWithdrawals.generate(start, end));
        result.addAll(paymentsToFamilyFriends.generate(start, end));
        result.addAll(groceries.generate(start, end));
        result.addAll(clothing.generate(start, end));
        result.addAll(salary.generate(start, end));
        result.addAll(magazine.generate(start, end));
        result.addAll(gym.generate(start, end));
        result.addAll(utilities.generate(start, end));

        Collections.sort(result, new Comparator<TransactionModel>() {
            @Override
            public int compare(TransactionModel o1, TransactionModel o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });


        result = cleanupAndFilter(result);
//        result.forEach(e -> LOG.info("TIME:{} AMOUNT:{} where: {}", e.getTimestamp(), e.getAmount(), e.getMerchant()));
        return result;

    }

    public static List<TransactionModel> cleanupAndFilter(List<TransactionModel> input) {
        List<TransactionModel> result = new ArrayList<>();

        result.add(input.get(0));
        for (int i = 1; i < input.size(); i++) {
            TransactionModel prev = input.get(i - 1);
            TransactionModel current = input.get(i);
            if (
                    !Arrays.asList(TransactionType.DIRECT_DEBIT, TransactionType.STANDING_ORDER, TransactionType.BACS).contains(current.getType())
                            &&
                            !prev.getMerchant().equalsIgnoreCase(current.getMerchant())
                            &&
                            prev.getTimestamp().plusMinutes(15).isAfter(current.getTimestamp())) {
                continue;
            }
            result.add(current);
        }
        Collections.sort(result, new Comparator<TransactionModel>() {
            @Override
            public int compare(TransactionModel o1, TransactionModel o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
        return result;
    }
}
