package kyjtheyj.coffeedrink.common.exception.domain;

import kyjtheyj.coffeedrink.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PointExceptionEnum implements ErrorCode {
    ERR_POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 정보가 존재하지 않아 진행할 수 없습니다")
    , ERR_POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "포인트 잔액이 부족합니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    PointExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
