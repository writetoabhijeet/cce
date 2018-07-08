package com.tiaa.cce.service;

import com.tiaa.cce.domain.CreditCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by USER on 08-07-2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardEngineServiceImplTest {

    @Autowired
    private CreditCardEngineServiceImpl creditCardEngineService;

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGenerateCreditCard() throws Exception {
        int expected = 10;
        int actual = creditCardEngineService.generateCreditCard("visa",10).size();
        assertEquals("Expected to generate 10 cc numbers",expected,actual);

        actual = creditCardEngineService.generateCreditCard("mastercard",10).size();
        assertEquals("Expected to generate 10 cc numbers",expected,actual);

        actual = creditCardEngineService.generateCreditCard("amex",10).size();
        assertEquals("Expected to generate 10 cc numbers",expected,actual);

        actual = creditCardEngineService.generateCreditCard("discover",10).size();
        assertEquals("Expected to generate 10 cc numbers",expected,actual);

        try{
            actual = creditCardEngineService.generateCreditCard("xyz",10).size();
        }catch(Exception e){
            assertEquals("Expected to exception","Invalid card type",e.getMessage());
        }

    }

    @Test
    public void testValidateCardList() throws Exception {

        List<String> cardNumberList = new ArrayList<String>();
        cardNumberList.add("6839469454888117");
        cardNumberList.add("6145812914649406");
        cardNumberList.add("6064868991371121");

        int expected = 3;
        List<CreditCard> actualList = creditCardEngineService.validateCardList(cardNumberList);

        assertEquals("Expected 3 valid cards",expected,actualList.size());

        cardNumberList.clear();
        cardNumberList.add("1234567890123456");
        actualList = creditCardEngineService.validateCardList(cardNumberList);

        assertEquals("Expected invalid  cards","INVALID CARD", actualList.get(0).getExpiryDate());

    }
}