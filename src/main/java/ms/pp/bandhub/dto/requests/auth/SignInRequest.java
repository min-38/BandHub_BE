package ms.pp.bandhub.dto.requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record SignInRequest (
    @Email(message = "이메일 형식이 아닙니다.")
    String email,
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$", message = "비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
    String password,
    boolean keepLogin
) {}
