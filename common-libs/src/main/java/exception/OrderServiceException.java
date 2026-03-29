package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class OrderServiceException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public OrderServiceException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage());
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
