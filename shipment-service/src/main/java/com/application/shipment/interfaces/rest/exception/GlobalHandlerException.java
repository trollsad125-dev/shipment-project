package com.application.shipment.interfaces.rest.exception;

import com.application.shipment.application.exception.ShipmentNotFoundException;
import com.application.shipment.domain.exception.InvalidShipmentStateException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalHandlerException {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleShipmentNotFound(ShipmentNotFoundException exception, HttpServletRequest request){
        ApiErrorCode errorCode = ApiErrorCode.RESOURCE_NOT_FOUND;
        ApiErrorDetails detailCode = ApiErrorDetails.SHIPMENT_NOT_FOUND;

        ApiErrorResponse apiRes =
                new ApiErrorResponse(
                        Instant.now(),
                        HttpStatus.NOT_FOUND.value(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        request.getRequestURI(),
                        null,
                        List.of(
                                new ApiErrorResponse.ErrorResponse(
                                        null,
                                        detailCode.getCode(),
                                        detailCode.formatMessage(exception.getShipmentId())
                                )
                        )
                        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiRes);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMismatch(MethodArgumentTypeMismatchException exception, HttpServletRequest request){
        ApiErrorCode errorCode = ApiErrorCode.VALIDATION_FAILED;

        ApiErrorDetails detailCode = ApiErrorDetails.INVALID_PARAMETER;

        boolean isUuid = UUID.class.equals(exception.getRequiredType());
        if (isUuid) {
            detailCode = ApiErrorDetails.INVALID_UUID;
        }

        ApiErrorResponse apiRes =
                new ApiErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        request.getRequestURI(),
                        null,
                        List.of(
                                new ApiErrorResponse.ErrorResponse(
                                        exception.getName(),
                                        detailCode.getCode(),
                                        detailCode.formatMessage(exception.getName())
                                )
                        )
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiRes);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotValid(MethodArgumentNotValidException methodExcep, HttpServletRequest request){
        ApiErrorCode errorCode = ApiErrorCode.VALIDATION_FAILED;

        List<ApiErrorResponse.ErrorResponse> listErrorResponse = getListErrorResponse(methodExcep);
        ApiErrorResponse apiRes =
                new ApiErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        request.getRequestURI(),
                        null,
                        listErrorResponse
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiRes);

    }

    private static @NonNull List<ApiErrorResponse.ErrorResponse> getListErrorResponse(MethodArgumentNotValidException methodExcep) {
        ApiErrorDetails detailCode = ApiErrorDetails.INVALID_PARAMETER;
        List<ApiErrorResponse.ErrorResponse> listErrorResponse = new ArrayList<>();
        List<FieldError> fieldErrorList = methodExcep.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrorList){
            String message =fieldError.isBindingFailure()
                    ? detailCode.formatMessage(fieldError.getField())
                    : fieldError.getDefaultMessage();
            listErrorResponse.add(new ApiErrorResponse.ErrorResponse(fieldError.getField(),detailCode.getCode(),message));
        }
        return listErrorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {

        log.error("Data integrity violation on {}", request.getRequestURI(), exception);

        return buildDatabaseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCode.INTERNAL_ERROR,
                ApiErrorDetails.DATA_INTEGRITY_VIOLATION,
                request
        );
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(
            OptimisticLockingFailureException exception,
            HttpServletRequest request) {

        log.warn("Concurrent update on {}", request.getRequestURI(), exception);

        return buildDatabaseError(
                HttpStatus.CONFLICT,
                ApiErrorCode.CONFLICT,
                ApiErrorDetails.CONCURRENT_MODIFICATION,
                request
        );
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccess(
            DataAccessException exception,
            HttpServletRequest request) {

        log.error("Database operation failed on {}", request.getRequestURI(), exception);

        return buildDatabaseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCode.INTERNAL_ERROR,
                ApiErrorDetails.DATABASE_ERROR,
                request
        );
    }

    private ResponseEntity<ApiErrorResponse> buildDatabaseError(
            HttpStatus status,
            ApiErrorCode errorCode,
            ApiErrorDetails detailCode,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI(),
                null,
                List.of(
                        new ApiErrorResponse.ErrorResponse(
                                null,
                                detailCode.getCode(),
                                detailCode.formatMessage()
                        )
                )
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(InvalidShipmentStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidShipmentState(
            InvalidShipmentStateException exception,
            HttpServletRequest request) {

        ApiErrorCode errorCode = ApiErrorCode.BUSINESS_RULE_VIOLATION;
        ApiErrorDetails detailCode = ApiErrorDetails.INVALID_SHIPMENT_STATE;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                request.getRequestURI(),
                null,
                List.of(
                        new ApiErrorResponse.ErrorResponse(
                                null,
                                detailCode.getCode(),
                                exception.getMessage()
                        )
                )
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
