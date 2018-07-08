package com.tiaa.cce.service;

import com.tiaa.cce.domain.CreditCard;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by USER on 08-07-2018.
 */
public interface CreditCardEngineService {

    List<String> generateCreditCard(String cardType, long numberOfCards) throws Exception;
    List<CreditCard> validateCardList(List<String> cardNumberList) throws InterruptedException, ExecutionException;

}
