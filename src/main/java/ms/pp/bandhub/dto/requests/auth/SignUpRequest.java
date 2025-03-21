package ms.pp.bandhub.dto.requests.auth;

import lombok.Getter;

@Getter
public class SignUpRequest {
    String email;
    String nickname;
    String password;
    String turnstile;
}
