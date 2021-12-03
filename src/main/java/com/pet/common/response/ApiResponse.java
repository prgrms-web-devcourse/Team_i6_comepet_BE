package com.pet.common.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;

@Value
@Getter
public class ApiResponse<T> {

    T data;

    LocalDateTime serverDateTime;

    public ApiResponse(T data) {
        ObjectUtils.requireNonEmpty(data, "data must be not null");
        this.data = data;
        this.serverDateTime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(final T data) {
        return new ApiResponse<>(data);
    }

}
