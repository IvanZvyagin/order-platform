package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderServiceException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public OrderServiceException(ErrorCode errorCode) {
        this(errorCode, errorCode.defaultMessage);
    }

    public enum ErrorCode {
        PRODUCT_NOT_FOUND("Товар не найден"),
        ORDER_NOT_FOUND("Заказ не найден"),
        INSUFFICIENT_STOCK("Недостаточно товара на складе"),
        USER_NOT_FOUND("Пользователь не найден"),
        ORDER_CREATION_FAILED("Ошибка создания заказа"),
        AUTHENTICATION_FAILED("Ошибка аутентификации"),
        UNAUTHORIZED("Не авторизован");

        private final String defaultMessage;

        ErrorCode(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }
}
