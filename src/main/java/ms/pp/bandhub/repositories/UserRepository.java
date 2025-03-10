package ms.pp.bandhub.repositories;

import ms.pp.bandhub.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일을 기준으로 유저 찾기
    Optional<User> findByEmail(String email);
}

