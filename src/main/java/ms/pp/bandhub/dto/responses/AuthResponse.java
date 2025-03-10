package ms.pp.bandhub.dto.responses;

public class AuthResponse extends ResponseDTO {
    public AuthResponse(boolean status, String message) {
        super(status, message);
    }

    public static AuthResponse of(boolean status, String message) {
        return new AuthResponse(status, message);
    }
}