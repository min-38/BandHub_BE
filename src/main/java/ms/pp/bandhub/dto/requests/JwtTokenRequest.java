package ms.pp.bandhub.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenRequest {
    private String refreshToken;
}