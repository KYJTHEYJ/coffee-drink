package kyjtheyj.coffeedrink.common.exception.domain;

import kyjtheyj.coffeedrink.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberExceptionEnum implements ErrorCode {
    ERR_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MemberExceptionEnum(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
