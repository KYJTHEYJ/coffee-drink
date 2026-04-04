package kyjtheyj.coffeedrink.common.exception;

import kyjtheyj.coffeedrink.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceErrorException.class)
    public ResponseEntity<BaseResponse<Void>> handleServiceErrorException(ServiceErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(BaseResponse.fail(e.getHttpStatus().name(), e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        log.error("회원 인증 에러 발생", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.fail(HttpStatus.UNAUTHORIZED.name(), "이메일 또는 비밀번호가 틀립니다"));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("데이터 유효성 에러 발생 : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(HttpStatus.BAD_REQUEST.name(), e.getAllErrors().getFirst().getDefaultMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("데이터 JSON 변환 에러 발생 : ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.fail(HttpStatus.BAD_REQUEST.name(), "올바른 값이 아닙니다"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleCriticalErrorException(Exception e) {
        log.error("서버 에러 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), "서버 오류로 인해 잠시 후 다시 시도하시기 바랍니다"));
    }
}
