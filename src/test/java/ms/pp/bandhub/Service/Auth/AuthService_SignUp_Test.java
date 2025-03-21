package ms.pp.bandhub.Service.Auth;

import ms.pp.bandhub.Service.AuthService;
import ms.pp.bandhub.Service.CaptchaService;
import ms.pp.bandhub.dto.responses.auth.SignUpResponse;
import ms.pp.bandhub.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthService_SignUp_Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CaptchaService captchaService;

    @InjectMocks
    private AuthService authService;

    String email = "test@test.com";
    String nickname = "tester";
    String password = "test1234";
    String turnstile = "testkey";

    @Test
    @DisplayName("사용자 회원가입 테스트 - 정상 회원가입")
    void signup_success_test() {
        // Given (가짜 데이터 설정)
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByNickname(nickname)).thenReturn(false);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(true);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // When (테스트 실행)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        // Then (검증)
        assertTrue(response.isStatus());
        assertEquals("회원가입 성공", response.getMessage());
    }

    @Test
    @DisplayName("사용자 회원가입 테스트 - 존재하는 이메일일 때")
    void signup_fail_exist_email_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.existsByNickname(nickname)).thenReturn(false);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(true);

        // When & Then (예외 발생 검증)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        assertFalse(response.isStatus());
        assertEquals("", response.getMessage());
        assertEquals("중복 이메일", response.getErrors().get("email"));
    }

    @Test
    @DisplayName("사용자 회원가입 테스트 - 존재하는 닉네임일 때")
    void signup_fail_exist_nickname_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByNickname(nickname)).thenReturn(true);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(true);

        // When & Then (예외 발생 검증)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        assertFalse(response.isStatus());
        assertEquals("", response.getMessage());
        assertEquals("중복 닉네임", response.getErrors().get("nickname"));
    }

    @Test
    @DisplayName("사용자 회원가입 테스트 - turnstile 실패 때")
    void signup_fail_turnstile_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByNickname(nickname)).thenReturn(false);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(false);

        // When & Then (예외 발생 검증)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        assertFalse(response.isStatus());
        assertEquals("", response.getMessage());
        assertEquals("recaptcha 실패", response.getErrors().get("recaptcha"));
    }

    @Test
    @DisplayName("사용자 회원가입 테스트 - 존재하는 이메일, 닉네임일 때")
    void signup_fail_exist_email_and_nickname_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.existsByNickname(nickname)).thenReturn(true);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(true);

        // When & Then (예외 발생 검증)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        assertFalse(response.isStatus());
        assertEquals("", response.getMessage());
        assertEquals("중복 이메일", response.getErrors().get("email"));
        assertEquals("중복 닉네임", response.getErrors().get("nickname"));
    }

    @Test
    @DisplayName("사용자 회원가입 테스트 - 존재하는 이메일, 닉네임이고 recaptcha 실패 때")
    void signup_fail_exist_email_and_nickname_and_recaptcha_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.existsByNickname(nickname)).thenReturn(true);
        when(captchaService.verifyTurnstile(turnstile)).thenReturn(false);

        // When & Then (예외 발생 검증)
        SignUpResponse response = authService.signup(email, nickname, password, turnstile);

        assertFalse(response.isStatus());
        assertEquals("", response.getMessage());
        assertEquals("중복 이메일", response.getErrors().get("email"));
        assertEquals("중복 닉네임", response.getErrors().get("nickname"));
        assertEquals("recaptcha 실패", response.getErrors().get("recaptcha"));
    }
}
