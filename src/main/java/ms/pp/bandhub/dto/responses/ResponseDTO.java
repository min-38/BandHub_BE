package ms.pp.bandhub.dto.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ResponseDTO {
    private final boolean status;
    private final String message;

    public static ResponseDTO of(boolean status, String message) {
        return new ResponseDTO(status, message);
    }
}
