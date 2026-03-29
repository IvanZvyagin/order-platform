package notificationservice.exception;

import exception.ErrorResponse;
import exception.OrderServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ErrorResponse> handleOrderServiceException(OrderServiceException ex){
        log.error("Order service exception: code={}, message={}", ex.getErrorCode(), ex.getMessage());
        HttpStatus status = ex.getHttpStatus();
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode().name(),
                ex.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
        log.error("Unexpected error", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}