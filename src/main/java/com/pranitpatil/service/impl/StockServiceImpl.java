package com.pranitpatil.service.impl;

import com.pranitpatil.config.StockManagementProperties;
import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.StockDto;
import com.pranitpatil.entity.Stock;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.exception.StockLockedException;
import com.pranitpatil.repository.StockRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockServiceImpl implements StockService{

    private StockRepository stockRepository;
    private ModelMapper modelMapper;
    private StockManagementProperties stockManagementProperties;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, ModelMapper modelMapper,
                            StockManagementProperties stockManagementProperties) {
        this.stockRepository = stockRepository;
        this.modelMapper = modelMapper;
        this.stockManagementProperties = stockManagementProperties;
    }

    @Override
    public StockDto findStockById(long id) {
        return modelMapper.map(stockRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Stock with id " + id + " not found.")),
                StockDto.class);
    }

    @Override
    public PagedResponse<StockDto> findAllStocks(Pageable pageable) {
        Page<Stock> page = stockRepository.findAll(pageable);
        List<StockDto> stocks = modelMapper.map(page.getContent(), new TypeToken<List<StockDto>>() {}.getType());

        PagedResponse<StockDto> pagedResponse = new PagedResponse<>();
        pagedResponse.setEntity(stocks);
        pagedResponse.setPageNumber(page.getNumber());
        pagedResponse.setTotalItems(page.getTotalElements());
        pagedResponse.setTotalPages(page.getTotalPages());

        return pagedResponse;
    }

    @Override
    public void deleteStock(long id) {
        Stock stockEntity = stockRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Stock with id " + id + " not found."));
        checkStockIsLocked(stockEntity);
        stockRepository.deleteById(id);
    }

    @Override
    public StockDto saveStock(StockDto stock) {
        Stock stockEntity = modelMapper.map(stock, Stock.class);
        stockEntity = stockRepository.save(stockEntity);
        return modelMapper.map(stockEntity, StockDto.class);
    }

    @Override
    public StockDto updateStock(StockDto stock) {

        Stock stockEntity = stockRepository.findById(stock.getId()).orElseThrow(
                () -> new NotFoundException("Stock with id " + stock.getId() + " not found."));

        checkStockIsLocked(stockEntity);

        stockEntity.setName(stock.getName());
        stockEntity.setPrice(stock.getPrice());
        stockEntity = stockRepository.save(stockEntity);
        return modelMapper.map(stockEntity, StockDto.class);
    }

    private void checkStockIsLocked(Stock stockEntity) {
        long lockDuration = Duration.between(stockEntity.getLastModifiedAt(), LocalDateTime.now()).toSeconds();
        if(lockDuration < stockManagementProperties.getLockingInterval()){
            throw new StockLockedException("This stock is locked for updates.");
        }
    }
}
