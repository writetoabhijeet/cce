package com.tiaa.cce.web;

import com.tiaa.cce.service.CreditCardEngineServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by USER on 08-07-2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CreditCardEngineControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CreditCardEngineServiceImpl creditCardEngineService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetCreditCards() throws Exception {
        this.mockMvc.perform(get("/CCEngine/visa/10")).andExpect(status().is(200));
    }

    @Test
    public void testValidateCardNumbers() throws Exception {
      String reqBody =  "[\n" +
                "    \"9391726744312541\",\n" +
                "    \"6963313171276008\",\n" +
                "    \"6839469454888117\",\n" +
                "    \"6145812914649406\",\n" +
                "    \"6850293602444738\",\n" +
                "    \"6064868991371121\",\n" +
                "    \"6084777616804349\",\n" +
                "    \"6474466621291597\",\n" +
                "    \"6907724732190406\",\n" +
                "    \"6508231816368109\"\n" +
                "]" ;
        this.mockMvc.perform(post("/CCEngine/validateCards").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isBadRequest());


        this.mockMvc.perform(post("/CCEngine/validateCards").contentType(MediaType.APPLICATION_JSON)
                .content(reqBody)).andExpect(status().isOk());

    }
}