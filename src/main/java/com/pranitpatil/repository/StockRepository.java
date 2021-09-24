package com.pranitpatil.repository;

import com.pranitpatil.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock, Long> {

    @Override
    Optional<Stock> findById(Long aLong);

    @Override
    Page<Stock> findAll(Pageable pageable);
}
