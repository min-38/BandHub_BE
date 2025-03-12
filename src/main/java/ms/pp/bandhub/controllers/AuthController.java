package ms.pp.bandhub.controllers;

import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.Service.AuthService;
import ms.pp.bandhub.dto.responses.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signin")
    public ResponseEntity<LoginResponse> signin(@RequestParam("email") String email, @RequestParam("password") String password)
    {
        LoginResponse response = authService.signin(email, password);

        // 로그인 실패 시 401 Unauthorized 응답 반환
        if (!response.isStatus())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        return ResponseEntity.ok(response);
    }
}
