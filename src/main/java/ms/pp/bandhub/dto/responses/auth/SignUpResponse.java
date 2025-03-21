package ms.pp.bandhub.dto.responses.auth;

import lombok.Builder;
import lombok.Getter;
import ms.pp.bandhub.dto.responses.ResponseDTO;

import java.util.Map;

@Getter
public class SignUpResponse extends ResponseDTO {
    private Map<String, String> errors; // 필드별 에러 메시지

    @Builder
    public SignUpResponse (boolean status, String message) {
        super(status, message);
    }

    @Builder
    public SignUpResponse (boolean status, Map<String, String> errors) {
        super(status, "");
        this.errors = errors;
    }
}
