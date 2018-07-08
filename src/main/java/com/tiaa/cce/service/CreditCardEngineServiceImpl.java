package com.tiaa.cce.service;

import com.tiaa.cce.domain.CreditCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by USER on 08-07-2018.
 */
@Slf4j
@Service
public class CreditCardEngineServiceImpl implements CreditCardEngineService {
    private static final int CARD_LENGTH = 16;
    private Random random = new Random(System.currentTimeMillis());

    private static final Map<String, String> cardTypeMap = new ConcurrentHashMap<String, String>();

    static {
        cardTypeMap.put("VISA", "4");
        cardTypeMap.put("MASTERCARD", "5");
        cardTypeMap.put("AMEX", "37");
        cardTypeMap.put("DISCOVER", "6");
    }

    @Override
    public List<String> generateCreditCard(String cardType, long numberOfCards) throws Exception {

        String startWith = cardTypeMap.get(cardType.toUpperCase());
        List<String> cardList = new ArrayList<String>();
        if (startWith == null) {
            throw new Exception("Invalid card type");
        } else {

            for (int i = 0; i < numberOfCards; i++) {
                cardList.add(getValidCreditCardNumber(startWith));
            }
        }
        return cardList;
    }


    private String getValidCreditCardNumber(String cardPrefix) {

        int cardNumLength = CARD_LENGTH - (cardPrefix.length() + 1);

        StringBuilder builder = new StringBuilder(cardPrefix);
        for (int i = 0; i < cardNumLength; i++) {
            int digit = this.random.nextInt(10);
            builder.append(digit);
        }

        // Do the Luhn algorithm to generate the check digit.
        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();

    }

    /**
     * Generates the check digit required to make the given credit card number
     * valid (i.e. pass the Luhn check)
     *
     * @param number The credit card number for which to generate the check digit.
     * @return The check digit required to make the given credit card number valid.
     */
    private int getCheckDigit(String number) {


        // The digits we need to replace will be those in an even position for
        // card numbers whose length is an even number, or those is an odd
        // position for card numbers whose length is an odd number. This is
        // because the Luhn algorithm reverses the card number, and doubles
        // every other number starting from the second number from the last
        // position.
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));
            // Double every second digit from right to left. If doubling of a digit results in a two-digit number,
            // add up the two digits to get a single-digit number
            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        // The check digit is the number required to make the sum a multiple of
        // 10.
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

    @Override
    public List<CreditCard> validateCardList(List<String> cardNumberList) throws InterruptedException, ExecutionException {
        List<CreditCard> result = new ArrayList<CreditCard>();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(cardNumberList.size());
        Set<Callable<CreditCard>> callables = new HashSet<Callable<CreditCard>>();

        for (int i = 0; i < cardNumberList.size(); i++) {
            callables.add(new CreditCardValidator(cardNumberList.get(i)));
        }

        List<Future<CreditCard>> futures = executor.invokeAll(callables);
        for (Future<CreditCard> future : futures) {
            CreditCard cc = future.get();
            if (cc != null) {
                result.add(cc);
            }
        }

        executor.shutdown();
        ;
        return result;

    }

    class CreditCardValidator implements Callable<CreditCard> {

        private CreditCard creditCard;
        private String cardNumber;


        CreditCardValidator(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        @Override
        public CreditCard call() throws Exception {

            if (isValidCreditCardNumber(cardNumber)) {
                return new CreditCard(cardNumber, generateExpDate());
            } else {
                return new CreditCard(cardNumber, "INVALID CARD");
            }
        }

        public String generateExpDate() {
            LocalDate today = LocalDate.now();
            return today.format(DateTimeFormatter.ofPattern("MMYY"));
        }
    }

    /**
     * Luhn check or Mod 10 validation is used to validate credit card number.Below are the steps.
     * 1) Double every second digit from right to left. If doubling of a digit results in a two-digit number,
     * add up the two digits to get a single-digit number.
     * 2) Now add all single-digit numbers from Step 1.
     * 3) Add all digits in the odd places from right to left in the card number.
     * 4) Sum the results from Step 2 and Step 3.
     * 5) If the result from Step 4 is divisible by 10, the card number is valid; otherwise, it is invalid.
     */
    public boolean isValidCreditCardNumber(String creditCardNumber) {
        boolean isValid = false;

        try {
            String reversedNumber = new StringBuffer(creditCardNumber)
                    .reverse().toString();
            int mod10Count = 0;
            for (int i = 0; i < reversedNumber.length(); i++) {
                int augend = Integer.parseInt(String.valueOf(reversedNumber
                        .charAt(i)));
                if (((i + 1) % 2) == 0) {
                    String productString = String.valueOf(augend * 2);
                    augend = 0;
                    for (int j = 0; j < productString.length(); j++) {
                        augend += Integer.parseInt(String.valueOf(productString
                                .charAt(j)));
                    }
                }

                mod10Count += augend;
            }

            if ((mod10Count % 10) == 0) {
                isValid = true;
            }
        } catch (NumberFormatException e) {
            log.error("Number format exception while parsing Card number", e);
        }

        return isValid;
    }
}
