package com.rbs.hackathon.txeventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbs.hackathon.txeventsourcing.api.Transaction;
import com.rbs.hackathon.txeventsourcing.model.TransactionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class TxGeneratorStream {

    private static Logger LOG = LoggerFactory.getLogger(TxGeneratorStream.class.getSimpleName());

    private RestTemplate restTemplate = new RestTemplate();
    private String txConsumerUrl = null;

    public static void main(String[] args) {

        ZonedDateTime start = ZonedDateTime.of(2015, 6, 1, 9, 0, 0, 0, ZoneId.of("Europe/London"));
        new TxGeneratorStream().stream(start, 1000.00);

    }

    public void stream(ZonedDateTime start, double startBalance) {
        double currentBalance = startBalance;

        while (true) {
            ZonedDateTime end = start.plusMonths(3);
            List<TransactionModel> transactions = new TxGeneratorRunner().generateAll("123242453", start, end);


            for (TransactionModel txModel : transactions) {

                Transaction transaction = txModel.toApi();
                currentBalance = new BigDecimal(currentBalance + txModel.getAmount()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                transaction.setAccountBalance(currentBalance);
                LOG.info("TIME:{} AMOUNT:{} what: {} where: {} ({}) balance: {}",
                        transaction.getTransactionDateTime(),
                        transaction.getTransactionAmount(), transaction.getType(),
                        transaction.getTransactionDescription(), transaction.getCategory(),
                        transaction.getAccountBalance());

                send(transaction);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    //noop
                }
            }
            start = end.plusHours(1);
        }
    }

    public void send(Transaction t) {
        try {
            String request = new ObjectMapper().writeValueAsString(Arrays.asList(t));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setContentLength(request.getBytes().length);
            HttpEntity<String> stringHttpEntity = new HttpEntity<>(request, httpHeaders);
//            restTemplate.postForEntity(txConsumerUrl, stringHttpEntity, String.class);

        } catch (Exception e) {
            LOG.warn("Could not send tx: {}", e.getMessage(), e);
        }
    }
}
