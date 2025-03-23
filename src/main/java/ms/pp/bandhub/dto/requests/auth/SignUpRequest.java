package ms.pp.bandhub.dto.requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest (
    @Email(message = "이메일 형식이 아닙니다.")
    String email,
    @Pattern(regexp = "^[^~'/\\\\`\"]{4,10}$", message = "닉네임은 ~, ', \", /, \\를 제외한 4~10자여야 합니다.")
    String nickname,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$", message = "비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
    String password,
    String password_confirm,
    @NotNull(message = "recaptcha 토큰이 필요합니다.")
    String turnstile
) {}
