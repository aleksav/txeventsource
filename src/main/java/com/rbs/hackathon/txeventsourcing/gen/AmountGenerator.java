package com.rbs.hackathon.txeventsourcing.gen;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AmountGenerator {
    private double minAmount;
    private double maxAmount;
    private Double fixedAmount;
    private List<Double> allowedAmounts = new ArrayList<>();
    private boolean withPence = true;
    private boolean incoming = false;


    public static AmountGenerator fixed(double amount) {
        return new AmountGenerator().setFixedAmount(amount);
    }

    public static AmountGenerator random(double min, double max) {
        return new AmountGenerator().setMinAmount(min).setMaxAmount(max);
    }

    public static AmountGenerator discrete(Double... amounts) {
        return new AmountGenerator().setAllowedAmounts(Arrays.asList(amounts));
    }


    public double generate() {

        if (fixedAmount != null) {
            return sign(fixedAmount);
        }

        if (!allowedAmounts.isEmpty()) {
            return sign(allowedAmounts.get(RandomUtils.nextInt(allowedAmounts.size())));
        }
        int random = RandomUtils.nextInt(new Double(maxAmount - minAmount).intValue());
        double wholePounds = minAmount + random;
        double result;
        if (!withPence) {
            result = wholePounds;
        } else {
            result = wholePounds + RandomUtils.nextInt(20) * 0.05;
        }
        return sign(result);
    }

    private double sign(double input) {
        double res = Math.abs(input);
        res = new BigDecimal(res).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        return incoming ? res : -res;
    }
}
