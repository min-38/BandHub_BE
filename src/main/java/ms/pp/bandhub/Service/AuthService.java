package ms.pp.bandhub.Service;

import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.dto.responses.LoginResponse;
import ms.pp.bandhub.repositories.UserRepository;
import ms.pp.bandhub.security.jwt.JwtTokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
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
}