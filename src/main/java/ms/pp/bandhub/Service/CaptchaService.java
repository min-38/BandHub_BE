package ms.pp.bandhub.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CaptchaService {

    @Value("${turnstile.secret}")
    private String secretKey;

    @Value("${turnstile.url}")
    private String url;

    public boolean verifyTurnstile(String token) {
        if (secretKey == null)
            return true;

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", token);

        ResponseEntity<TurnstileResponse> response = restTemplate.postForEntity(url, params, TurnstileResponse.class);
        return response.getBody() != null && response.getBody().isSuccess();
    }

    public static class TurnstileResponse {
        @Getter
        private boolean success;

        @JsonProperty("error-codes")
        private List<String> errorCodes;
    }
}
