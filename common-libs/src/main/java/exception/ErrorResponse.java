package exception;

import java.time.LocalDateTime;

public record ErrorResponse(String errorCode,
                            String message,
                            int status,
                            LocalDateTime timestamp)
{}
