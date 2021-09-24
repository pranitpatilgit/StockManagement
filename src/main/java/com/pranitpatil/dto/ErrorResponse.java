package com.pranitpatil.dto;

import org.slf4j.MDC;

public class ErrorResponse {

    private String errorMessage;
    private String requestId;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.requestId = MDC.get("requestID");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRequestId() {
        return requestId;
    }
}
