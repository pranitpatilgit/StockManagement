package com.pranitpatil.exception;

public class StockLockedException extends RuntimeException{

    public StockLockedException(String message) {
        super(message);
    }
}
