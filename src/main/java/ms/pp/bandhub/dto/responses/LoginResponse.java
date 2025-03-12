package ms.pp.bandhub.dto.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse extends ResponseDTO {
    private String accessToken;    // Access Token
    private String refreshToken;   // Refresh Token

    @Builder
    public LoginResponse (boolean status, String message, String accessToken, String refreshToken) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}