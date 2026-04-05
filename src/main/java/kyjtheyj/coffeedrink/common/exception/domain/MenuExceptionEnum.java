package kyjtheyj.coffeedrink.common.exception.domain;

import kyjtheyj.coffeedrink.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MenuExceptionEnum implements ErrorCode {
    ERR_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다")
    , ERR_MENU_QUANTITY_LESS(HttpStatus.CONFLICT, "메뉴 수량이 부족합니다")
    , ERR_MENU_QUANTITY_INVALID(HttpStatus.CONFLICT, "조정 수량은 0이나 음수일 수 없습니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    MenuExceptionEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
