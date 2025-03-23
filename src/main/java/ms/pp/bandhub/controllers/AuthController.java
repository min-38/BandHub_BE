package ms.pp.bandhub.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.Service.AuthService;
import ms.pp.bandhub.dto.requests.auth.SignInRequest;
import ms.pp.bandhub.dto.requests.auth.SignUpRequest;
import ms.pp.bandhub.dto.responses.ResponseDTO;
import ms.pp.bandhub.dto.responses.auth.SignInResponse;
import ms.pp.bandhub.dto.responses.auth.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@Valid @RequestBody final SignInRequest signInRequest)
    {
        SignInResponse response = authService.signin(signInRequest);

        if (!response.isStatus())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody final SignUpRequest signUpRequest)
    {
        SignUpResponse response = authService.signup(signUpRequest);

        if (!response.isStatus())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check/email")
    public ResponseEntity<ResponseDTO> emailCheck(@RequestParam String email)
    {
        boolean isAvailable = authService.isEmailAvailable(email);
        return ResponseEntity.ok(new ResponseDTO(isAvailable, ""));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<ResponseDTO> nicknameCheck(@RequestParam String nickname)
    {
        boolean isAvailable = authService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(new ResponseDTO(isAvailable, ""));
    }
}
