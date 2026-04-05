package kyjtheyj.coffeedrink.common.exception.domain;

import kyjtheyj.coffeedrink.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PointExceptionEnum implements ErrorCode {
    ERR_POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 충전 정보가 없어 충전에 실패하였습니다");

    private final HttpStatus httpStatus;
    private final String message;

    PointExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
