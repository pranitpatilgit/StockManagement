package com.pranitpatil.service;

import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.StockDto;
import org.springframework.data.domain.Pageable;

public interface StockService {

    StockDto findStockById(long id);
    PagedResponse<StockDto> findAllStocks(Pageable pageable);
    void deleteStock(long id);
    StockDto saveStock(StockDto stock);
    StockDto updateStock(StockDto stock);

}
