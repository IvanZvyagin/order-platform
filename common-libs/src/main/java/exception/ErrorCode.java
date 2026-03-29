package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PRODUCT_NOT_FOUND("Товар не найден", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND("Заказ не найден", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK("Недостаточно товара на складе", HttpStatus.CONFLICT),
    USER_NOT_FOUND("Пользователь не найден", HttpStatus.NOT_FOUND),
    ORDER_CREATION_FAILED("Ошибка создания заказа",HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED("Ошибка аутентификации", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Не авторизован", HttpStatus.FORBIDDEN),
    PRODUCT_CHECK_FAILED("Ошибка проверки товара", HttpStatus.BAD_REQUEST),
    RESERVATION_FAILED("Ошибка резервирования", HttpStatus.CONFLICT);

    private final String defaultMessage;
    private final HttpStatus httpStatus;
}
