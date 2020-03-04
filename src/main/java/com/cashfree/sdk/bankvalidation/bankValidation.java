package com.cashfree.sdk.bankvalidation;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ThreadLocalRandom;

import com.cashfree.lib.constants.Constants.Environment;


import com.cashfree.lib.payout.clients.Payouts;

import com.cashfree.lib.payout.clients.Validation;

import com.cashfree.lib.payout.domains.request.BulkValidationRequest;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;
import com.cashfree.lib.payout.domains.response.BulkValidationResponse;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.logger.VerySimpleFormatter;

public class bankValidation {
    private static final Logger log = Logger.getLogger(bankValidation.class.getName());
    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new VerySimpleFormatter());
        log.addHandler(consoleHandler);
    }

    public static void main(String[] args) {
        Payouts payouts = Payouts.getInstance(
                Environment.PRODUCTION, "CF1848EZPSGLHWP9IUE2Y", "b8df7784dd3f38911294d3597764dd43f3016a48");
        log.info("" + payouts.init());

        boolean isTokenValid = payouts.verifyToken();
        log.info("" + isTokenValid);
        if (!isTokenValid) return;

        log.info("Token is verfied");


        Validation validation=new Validation(payouts);


        log.info("" + validation.validateBankDetails(
                "JOHN", "9908712345", "026291800001191", "YESB0000262"));
        log.info("" + validation.validateUPIDetails("Cashfree", "success@upi"));

        List<BulkValidationRequest.Payload> entries = new ArrayList<>();
        entries.add(new BulkValidationRequest.Payload()
                .setName("Sameera Cashfree")
                .setBankAccount("000890289871772")
                .setIfsc("SCBL0036078")
                .setPhone("9015991882"));
        entries.add(new BulkValidationRequest.Payload()
                .setName("Cashfree Sameera")
                .setBankAccount("0001001289877623")
                .setIfsc("SBIN0008752")
                .setPhone("9023991882"));
        String bulkValidationId = "javasdktest" + ThreadLocalRandom.current().nextInt(0, 1000000);
        BulkValidationRequest request = new BulkValidationRequest()
                .setBulkValidationId(bulkValidationId)
                .setEntries(entries);
        BulkValidationResponse bulkValidationResponse = validation.validateBulkBankActivation(request);
        log.info("" + bulkValidationResponse);
        if (bulkValidationResponse.getData() != null) {
            String status = bulkValidationResponse.getData().getBulkValidationId();
            if (CommonUtils.isNotBlank(status)) {
                log.info("" + validation.getBulkValidationStatus(status, null, null));
            }
        }
    }
}
