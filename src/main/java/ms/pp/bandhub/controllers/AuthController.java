package ms.pp.bandhub.controllers;

import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.Service.AuthService;
import ms.pp.bandhub.dto.responses.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponse> signin(@RequestParam("email") String email, @RequestParam("password") String password)
    {
        AuthResponse response = authService.signin(email, password);
        return ResponseEntity.ok(response);
    }
}
