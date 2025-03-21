package ms.pp.bandhub.Service;

import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.dto.responses.auth.LoginResponse;
import ms.pp.bandhub.dto.responses.auth.SignUpResponse;
import ms.pp.bandhub.repositories.UserRepository;
import ms.pp.bandhub.security.jwt.JwtTokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CaptchaService captchaService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    // 로그인 처리
    @Transactional(readOnly = true)
    public LoginResponse signin(String email, String password) {
        // 사용자 조회 없으면 null 반환
        User user = userRepository.findByEmail(email).orElse(null);

        // 존재하지 않은 이메일일 때도 비밀번호가 틀렸다고 리턴하여 사용자의 정보 감춤
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResponse(false, "이메일 또는 비밀번호가 일치하지 않습니다.", null, null); // 실패 응답
        }

        // JWT 토큰 생성
        String accessToken = jwtTokenGenerator.createAccessToken(user.getId());
        String refreshToken = jwtTokenGenerator.createRefreshToken(user.getId());

        return new LoginResponse(true, "로그인 성공", accessToken, refreshToken);
    }

    public SignUpResponse signup(String email, String nickname, String password, String turnstile) {
        Map<String, String> errors = new HashMap<>();

        if(isEmailAvailable(email))
            errors.put("email", "중복 이메일");

        if(isNicknameAvailable(nickname))
            errors.put("nickname", "중복 닉네임");

        if (!captchaService.verifyTurnstile(turnstile))
            errors.put("recaptcha", "recaptcha 실패");

        if (!errors.isEmpty())
            return new SignUpResponse(false, errors);

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(email, nickname, encodedPassword));

        return new SignUpResponse(true, "회원가입 성공");
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}