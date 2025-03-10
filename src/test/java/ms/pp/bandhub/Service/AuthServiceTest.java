package ms.pp.bandhub.Service;


import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.dto.responses.AuthResponse;
import ms.pp.bandhub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;      // 가짜 데이터베이스

    @Mock
    private PasswordEncoder passwordEncoder;    // 가짜 비밀번호 암호화 객체

    @InjectMocks
    private AuthService authService;            // 테스트 대상 (Mock 객체들을 주입받음)

    private User user;                          // 테스트용 유저 객체

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword"); // 실제로 저장될 암호화된 비밀번호
    }

    @Test
    @DisplayName("사용자 로그인 테스트 - 정상 로그인")
    void signin_success_test() {
        // Given (가짜 데이터 설정)
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        // When (테스트 실행)
        AuthResponse response = authService.signin("test@example.com", "password123");

        // Then (검증)
        assertTrue(response.isStatus()); // 성공 상태 확인
        assertEquals("로그인 성공", response.getMessage()); // 메시지 확인
    }

    @Test
    @DisplayName("사용자 로그인 테스트 - 없는 이메일일 때")
    void signin_fail_test() {
        // Given (이메일이 없는 경우)
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When & Then (예외 발생 검증)
        AuthResponse response = authService.signin("notfound@example.com", "wrongpassword");

        assertEquals("이메일 또는 비밀번호가 일치하지 않습니다.", response.getMessage());
    }

    @Test
    @DisplayName("사용자 로그인 테스트 - 패스워드가 틀렸을 때")
    void signin_password_incoreect_test() {
        // Given (올바른 이메일이지만 비밀번호가 틀린 경우)
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // When
        AuthResponse response = authService.signin("test@example.com", "wrongpassword");

        // Then (로그인 실패 확인)
        assertFalse(response.isStatus()); // 실패 상태 확인
        assertEquals("이메일 또는 비밀번호가 일치하지 않습니다.", response.getMessage()); // 메시지 확인
    }
}
