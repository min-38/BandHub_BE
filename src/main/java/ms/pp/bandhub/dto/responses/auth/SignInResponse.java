package ms.pp.bandhub.dto.responses.auth;

import lombok.Builder;
import lombok.Getter;
import ms.pp.bandhub.dto.responses.ResponseDTO;

@Getter
public class SignInResponse extends ResponseDTO {
    private String accessToken;    // Access Token
    private String refreshToken;   // Refresh Token

    @Builder
    public SignInResponse (boolean status, String message, String accessToken, String refreshToken) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}