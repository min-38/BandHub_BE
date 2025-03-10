package ms.pp.bandhub.Service;

import lombok.RequiredArgsConstructor;
import ms.pp.bandhub.domains.User;
import ms.pp.bandhub.dto.responses.AuthResponse;
import ms.pp.bandhub.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 처리
    @Transactional(readOnly = true)
    public AuthResponse signin(String email, String password) {
        // 사용자 조회
        Optional<User> user = userRepository.findByEmail(email);

        /*
            비밀번호 확인
            존재하지 않은 이메일일 때도 비밀번호가 틀렸다고 리턴하여 사용자의 정보 감춤
        */
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword()))
            return AuthResponse.of(false, "이메일 또는 비밀번호가 일치하지 않습니다."); // 실패
        return AuthResponse.of(true, "로그인 성공"); // 성공
    }
}