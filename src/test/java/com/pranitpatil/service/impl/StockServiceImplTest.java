package com.pranitpatil.service.impl;

import com.pranitpatil.config.StockManagementProperties;
import com.pranitpatil.dto.PagedResponse;
import com.pranitpatil.dto.StockDto;
import com.pranitpatil.entity.Stock;
import com.pranitpatil.exception.NotFoundException;
import com.pranitpatil.exception.StockLockedException;
import com.pranitpatil.repository.StockRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockManagementProperties stockManagementProperties;

    private StockServiceImpl stockService;

    @Before
    public void before(){
        stockService = new StockServiceImpl(stockRepository, modelMapper, stockManagementProperties);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void givenStockId_whenFindStockById_thenReturnStock(){
        Stock stockEntity = new Stock();
        stockEntity.setId(1L);
        stockEntity.setName("Google");
        stockEntity.setPrice(BigDecimal.valueOf(123.45));

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stockEntity));

        StockDto stockDto = stockService.findStockById(1L);

        Assert.assertEquals(stockEntity.getId(), stockDto.getId());
        Assert.assertEquals(stockEntity.getName(), stockDto.getName());
        Assert.assertEquals(stockEntity.getPrice(), stockDto.getPrice());
    }

    @Test(expected = NotFoundException.class)
    public void givenInvalidStockId_whenFindStockById_thenThrowNotFoundException(){
        when(stockRepository.findById(1L)).thenThrow(new NotFoundException("Stock with id 1 not found."));

        stockService.findStockById(1L);

        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage("Stock with id 1 not found.");
    }

    @Test
    public void givenPagable_whenFindAllStocks_thenGetPaginatedResponse(){
        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock();
        stock.setId(1);

        stocks.add(stock);

        Pageable pageable = PageRequest.of(0, 10);

        when(stockRepository.findAll(pageable)).thenReturn(new PageImpl<>(stocks));

        PagedResponse<StockDto> response = stockService.findAllStocks(pageable);

        Assert.assertEquals(1, response.getEntity().get(0).getId());
    }

    @Test
    public void givenStockId_whenDeleteStock_thenStockIsDeleted(){
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLastModifiedAt(LocalDateTime.now().minusMinutes(10));

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        stockService.deleteStock(1L);

        verify(stockRepository).deleteById(1L);
    }

    @Test(expected = StockLockedException.class)
    public void givenLockedStockId_whenDeleteStock_thenThrowStockLockedException(){
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLastModifiedAt(LocalDateTime.now());

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockManagementProperties.getLockingInterval()).thenReturn(300);

        stockService.deleteStock(1L);

        expectedEx.expect(StockLockedException.class);
        expectedEx.expectMessage("This stock is locked for updates.");
    }

    @Test
    public void givenStock_whenSaveStock_thenReturnSavedStock(){
        StockDto stockDto = new StockDto();
        stockDto.setName("Google");

        Stock stock = new Stock();
        stock.setName("Google");
        stock.setId(1L);

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        StockDto result = stockService.saveStock(stockDto);

        stockService.saveStock(stockDto);

        Assert.assertEquals(1L, result.getId());
        Assert.assertEquals(stock.getId(), result.getId());
    }

    @Test
    public void givenStock_whenUpdateStock_thenStockIsUpdated(){
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setName("OLD");
        stock.setLastModifiedAt(LocalDateTime.now().minusMinutes(10));

        Stock newStock = new Stock();
        newStock.setId(1L);
        newStock.setName("NEW");
        newStock.setLastModifiedAt(LocalDateTime.now().minusMinutes(10));

        StockDto stockDto = new StockDto();
        stockDto.setId(1L);
        stockDto.setName("NEW");

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(newStock)).thenReturn(newStock);

        StockDto result = stockService.updateStock(stockDto);

        Assert.assertEquals(1L, result.getId());
        Assert.assertEquals(stockDto.getName(), result.getName());
    }

    @Test(expected = StockLockedException.class)
    public void givenStock_whenUpdateStock_thenStockLockedException(){
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setName("OLD");
        stock.setLastModifiedAt(LocalDateTime.now());

        StockDto stockDto = new StockDto();
        stockDto.setId(1L);
        stockDto.setName("NEW");

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockManagementProperties.getLockingInterval()).thenReturn(300);

        StockDto result = stockService.updateStock(stockDto);

        expectedEx.expect(StockLockedException.class);
        expectedEx.expectMessage("This stock is locked for updates.");
    }
}
