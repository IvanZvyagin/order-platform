package orderservice.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link orderservice.domain.entity.UserEntity}
 */
public record RegisterUserRequestDto(
        /**
         * Имя пользователя.
         * Должно содержать от 3 до 50 символов.
         */

        @NotBlank
        @Size(min = 3, max = 50)
        String username,
        /**
         * Пароль пользователя.
         * Должен содержать от 8 до 100 символов.
         */
        @NotBlank
        @Size(min = 8, max = 100)
        String password,
        /**
         * Подтверждение пароля.
         * Должен содержать от 8 до 100 символов.
         */
        @NotBlank
        @Size(min = 8, max = 100)
        String confirmPassword
) {
    /**
     * Проверяет совпадение паролей.
     * @return true если password и confirmPassword совпадают.
     */
    @AssertTrue
    public boolean isPasswordsMatch(){
        if(password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}