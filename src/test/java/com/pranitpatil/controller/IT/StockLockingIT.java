package com.pranitpatil.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranitpatil.dto.StockDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext
public class StockLockingIT {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenUpdateExistingStock_whenStockIsUpdatedAgain_thenLockingError() throws Exception {
        StockDto stock = objectMapper.readValue(mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), StockDto.class);

        stock.setPrice(BigDecimal.valueOf(159.46));

        //Wait till lock expires
        Thread.sleep(10000);
        mvc.perform(MockMvcRequestBuilders.put("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("159.46"));

        //Reupdate the stock
        stock.setPrice(BigDecimal.valueOf(159.57));
        mvc.perform(MockMvcRequestBuilders.put("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.status().isLocked())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("This stock is locked for updates."));
    }
}
