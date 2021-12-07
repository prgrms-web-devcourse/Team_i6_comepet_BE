package com.pet.common.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> {

    private final T data;

    private final LocalDateTime serverDateTime;

    public ApiResponse(final T data) {
        ObjectUtils.requireNonEmpty(data, "data must be not null");
        this.data = data;
        this.serverDateTime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(data);
    }

}
