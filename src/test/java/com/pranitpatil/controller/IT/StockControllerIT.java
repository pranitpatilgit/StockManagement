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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext
public class StockControllerIT {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenStock_whenAddNewStock_thenNewStockIsCreated() throws Exception {
        StockDto stock = new StockDto();
        stock.setName("TEST");
        stock.setPrice(123.45);

        StockDto createdStock = objectMapper.readValue(mvc.perform(MockMvcRequestBuilders.post("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST"))
                .andReturn().getResponse().getContentAsString(), StockDto.class);

        //Verify from REST
        mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + createdStock.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(123.45));
    }

    @Test
    public void givenExistingStock_whenStockIsUpdated_thenNewDetailsAreStored() throws Exception {
        StockDto stock = objectMapper.readValue(mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(), StockDto.class);

        stock.setPrice(159.46);

        //Wait till lock expires
        Thread.sleep(10000);
        mvc.perform(MockMvcRequestBuilders.put("/api/stocks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("159.46"));

        //Verify from REST
        mvc.perform(MockMvcRequestBuilders.get("/api/stocks/" + stock.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(stock.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(stock.getPrice()));
    }

    @Test
    public void givenStockId_whenStockIsDeleted_thenHTTP204AndStockDoesNotExist() throws Exception {

        //Wait till lock expires
        Thread.sleep(10000);
        mvc.perform(MockMvcRequestBuilders.delete("/api/stocks/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //Verify that stock does not exist
        mvc.perform(MockMvcRequestBuilders.get("/api/stocks/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
