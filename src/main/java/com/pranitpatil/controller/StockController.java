package com.pranitpatil.controller;

import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.StockDto;
import com.pranitpatil.service.impl.StockService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get All Available Stocks\n" +
            "This is a API with pagination and sorting,\n" +
            "\n" +
            "Provide page details as query parameters.")
    public PagedResponse<StockDto> getAllProducts(@PageableDefault(page = 0, size = 10)
                                                      @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return stockService.findAllStocks(pageable);
    }

    @GetMapping("{stockId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets the details of the stock")
    public StockDto getStockById(@PathVariable long stockId){
        return stockService.findStockById(stockId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a new stock and returns it")
    public StockDto createNewStock(@RequestBody StockDto stock) {
        return stockService.saveStock(stock);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates an existing stock and returns it")
    public StockDto updateStock(@RequestBody StockDto stock) {
        return stockService.updateStock(stock);
    }

    @DeleteMapping("{stockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an existing stock")
    public void deleteStock(@PathVariable long stockId) {
        stockService.deleteStock(stockId);
    }
}
