package kyjtheyj.coffeedrink.domain.menu.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;

public record MenuRegisterRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다")
        @Size(max = 30, message = "메뉴 이름은 30자 이하여야 합니다")
        String name

        , @NotNull(message = "가격은 필수입니다")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
        BigInteger price

        , @Size(max = 100, message = "설명은 100자 이하여야 합니다")
        String description

        , @NotNull(message = "정렬 순서는 필수입니다")
        Integer sortNumber

        , @NotNull(message = "재고는 필수입니다")
        @Min(value = 0, message = "재고는 0개 이상이어야 합니다")
        Long quantity
) {
}
