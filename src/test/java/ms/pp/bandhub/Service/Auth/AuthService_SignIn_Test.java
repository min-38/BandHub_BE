package ms.pp.bandhub.Service.Auth;

import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthService_SignIn_Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @DisplayName("통합 테스트 - 정상 로그인")
    void signin_success_test() throws Exception {
        // When
        String json = """
            {
                "email": "test@example.com",
                "password": "Password123!@#"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf()) // L
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 없는 이메일")
    void signin_fail_email_test() throws Exception {
        // When
        String json = """
            {
                "email": "notfound@example.com",
                "password": "Password123!@#"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf()) // L
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 비밀번호 틀림")
    void signin_fail_password_test() throws Exception {
        // When
        String json = """
            {
                "email": "test@example.com",
                "password": "WrongPassword123!@#"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf()) // L
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 이메일과 비밀번호 둘 다 틀림")
    void signin_fail_email_and_password_test() throws Exception {
        // When
        String json = """
            {
                "email": "notfound@example.com",
                "password": "WrongPassword123!@#"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf()) // L
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 이메일 유효성 검사 실패")
    void signin_fail_email_validation_test() throws Exception {
        // When
        String json = """
            {
                "email": "notfound?",
                "password": "Password123!@#",
                "keepLogin": true
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())  // 응답 전체 로그로 찍기
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 패스워드 유효성 검사 실패")
    void signin_fail_password_validation_test() throws Exception {
        // When
        String json = """
            {
                "email": "test@example.com",
                "password": "qwer1234"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf()) // L
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 - 이메일, 패스워드 유효성 검사 실패")
    void signin_fail_email_and_password_validation_test() throws Exception {
        // When
        String json = """
            {
                "email": "notfound",
                "password": "qwer1234"
            }
        """;

        // Then
        mockMvc.perform(post("/api/auth/signin")  // 실제 API UR
                        .with(csrf())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.accessToken").isEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }
}
