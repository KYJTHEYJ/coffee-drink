package kyjtheyj.coffeedrink.common.exception;

import kyjtheyj.coffeedrink.common.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceErrorException.class)
    public ResponseEntity<BaseResponse<Void>> handleServiceErrorException(ServiceErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(BaseResponse.fail(e.getHttpStatus().name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleCriticalErrorException(Exception e) {
        log.error("서버 에러 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), "서버 오류로 인해 잠시 후 다시 시도하시기 바랍니다"));
    }
}
