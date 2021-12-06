package com.pet.common.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> {

    private T data;

    private LocalDateTime serverDateTime;

    public ApiResponse(T data) {
        ObjectUtils.requireNonEmpty(data, "data must be not null");
        this.data = data;
        this.serverDateTime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(data);
    }

}
