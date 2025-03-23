package ms.pp.bandhub.advice;

import jakarta.servlet.http.HttpServletRequest;
import ms.pp.bandhub.controllers.AuthController;
import ms.pp.bandhub.dto.responses.auth.SignInResponse;
import ms.pp.bandhub.dto.responses.auth.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String path = request.getRequestURI();  // 요청 경로 가져오기
        String message = "이메일 또는 비밀번호가 일치하지 않습니다.";

        if (path.contains("/auth/signin")) {

            // 로그인 요청 -> LoginResponse 반환
            SignInResponse response = new SignInResponse(false, message, null, null);
            return ResponseEntity.badRequest().body(response);
        } else if (path.contains("/auth/signup")) {

            // 회원가입 요청 -> SignUpResponse 반환
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            SignUpResponse response = new SignUpResponse(false, errors);
            return ResponseEntity.badRequest().body(response);
        }

        // 혹시 모를 기타 요청 → 공통 에러 메시지
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
