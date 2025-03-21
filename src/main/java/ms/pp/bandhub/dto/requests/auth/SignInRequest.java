package ms.pp.bandhub.dto.requests.auth;

import lombok.Getter;

@Getter
public class SignInRequest {
    String      email;
    String      password;
    boolean     keepLogin;
}
