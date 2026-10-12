package com.application.shipment.interfaces.rest.exception;

public enum ApiErrorCode {
    VALIDATION_FAILED("Request contains invalid parameters."),
    RESOURCE_NOT_FOUND("The requested resource was not found."),
    BUSINESS_RULE_VIOLATION("The operation violates a business rule."),
    INTERNAL_ERROR("An unexpected error occurred."),
    CONFLICT("The request conflicts with the current resource state.");

    private final String message;

    ApiErrorCode(String message) {
        this.message = message;
    }

    public String getCode() {
        return name();
    }

    public String getMessage() {
        return message;
    }
}
