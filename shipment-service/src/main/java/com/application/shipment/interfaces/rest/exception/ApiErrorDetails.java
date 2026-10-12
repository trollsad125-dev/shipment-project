package com.application.shipment.interfaces.rest.exception;

public enum ApiErrorDetails {

        INVALID_PARAMETER("Parameter '%s' has an invalid format."),
        INVALID_UUID("Parameter '%s' must be a valid UUID."),
        SHIPMENT_NOT_FOUND("Shipment not found: %s"),
        DATA_INTEGRITY_VIOLATION("Unable to save shipment data."),
        CONCURRENT_MODIFICATION(
            "Shipment was updated by another request. Reload it and try again."
        ),
        INVALID_SHIPMENT_STATE("Shipment state does not allow this operation."),
        DATABASE_ERROR("Unable to complete the database operation.");

        private final String messageTemplate;

    ApiErrorDetails(String messageTemplate) {
            this.messageTemplate = messageTemplate;
        }

        public String getCode() {
            return name();
        }

        public String formatMessage(Object... arguments) {
            return messageTemplate.formatted(arguments);
        }

}
