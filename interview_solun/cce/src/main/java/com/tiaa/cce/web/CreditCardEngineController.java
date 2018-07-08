package com.tiaa.cce.web;

import com.tiaa.cce.domain.CreditCard;
import com.tiaa.cce.service.CreditCardEngineServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by USER on 08-07-2018.
 */

@RestController
@RequestMapping("/CCEngine/")
@Slf4j
public class CreditCardEngineController {

    private final CreditCardEngineServiceImpl creditCardEngineService;

    @Autowired
    public CreditCardEngineController(CreditCardEngineServiceImpl creditCardEngineService) {
        this.creditCardEngineService = creditCardEngineService;
    }

    @GetMapping(path = "/{cardType}/{numberOfCards}")
    public List<String> getCreditCards(@PathVariable("cardType") final String cardType, @PathVariable("numberOfCards") final int numberOfCards) throws Exception {
        log.info("Generating {} cards of type {} ", numberOfCards,cardType);
        return this.creditCardEngineService.generateCreditCard(cardType,numberOfCards);
    }

    @PostMapping("/validateCards")
    public List<CreditCard> validateCardNumbers(@RequestBody List<String> cardNumbers) throws Exception{

        return creditCardEngineService.validateCardList(cardNumbers);
    }
}
