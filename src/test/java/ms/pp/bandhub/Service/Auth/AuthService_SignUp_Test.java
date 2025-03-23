package ms.pp.bandhub.Service.Auth;

import ms.pp.bandhub.Service.CaptchaService;
import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthService_SignUp_Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CaptchaService captchaService;

    @BeforeEach
    void setUp() {
        // 실제 유저 저장 (DB에 직접 저장)
        User user = new User();
        user.setEmail("test@example.com");
        user.setNickname("test");
        user.setPassword(passwordEncoder.encode("Password123!@#"));  // 비밀번호 암호화

        userRepository.save(user);
    }

    @Test
    @DisplayName("통합 테스트 - 정상 회원가입")
    void signup_success_test() throws Exception {
        // Given
        String json = """
            {
                "email": "newUser@example.com",
                "nickname": "newUser",
                "password": "Password123!@#",
                "password_confirm": "Password123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 존재하는 이메일일 때")
    void signup_fail_exist_email_test() throws Exception {
        // Given
        String json = """
            {
                "email": "test@example.com",
                "nickname": "newUser",
                "password": "Password123!@#",
                "password_confirm": "Password123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors.email").value("중복 이메일"));
    }

    @Test
    @DisplayName("통합 테스트 - 존재하는 닉네임일 때")
    void signup_fail_exist_nickname_test() throws Exception {
        // Given
        String json = """
            {
                "email": "newUser@example.com",
                "nickname": "test",
                "password": "Password123!@#",
                "password_confirm": "Password123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors.nickname").value("중복 닉네임"));
    }

    @Test
    @DisplayName("통합 테스트 - 비밀번호가 일치하지 않을 때")
    void signup_fail_password_confirm0_test() throws Exception {
        // Given
        String json = """
            {
                "email": "newUser@example.com",
                "nickname": "newUser",
                "password": "Password123!@#",
                "password_confirm": "WrongPassword123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors.password").value("비밀번호 불일치"));
    }

    @Test
    @DisplayName("통합 테스트 - recaptcha 실패")
    void signup_fail_recaptcha_test() throws Exception {
        // Given
        String json = """
            {
                "email": "newUser@example.com",
                "nickname": "newUser",
                "password": "Password123!@#",
                "password_confirm": "WrongPassword123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(false);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors.recaptcha").value("recaptcha 실패"));
    }

    @Test
    @DisplayName("통합 테스트 - 이메일 유효성 검사 실패")
    void signup_fail_exist_email_validation_test() throws Exception {
        // Given
        String json = """
            {
                "email": "newUser",
                "nickname": "newUser",
                "password": "Password123!@#",
                "password_confirm": "Password123!@#",
                "turnstile": "recaptcha"
            }
        """;

        // When
        when(captchaService.verifyTurnstile(anyString())).thenReturn(true);

        // Then
        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.errors.email").value("이메일 형식이 아닙니다."));
    }
}
