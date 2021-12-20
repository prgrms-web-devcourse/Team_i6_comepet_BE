package com.pet.common.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Api Response 테스트")
class ApiResponseTest {

    @Test
    @DisplayName("ApiResponse 생성 테스트")
    void newApiResponseTest() {
        ApiResponse<String> testResponse = ApiResponse.ok("Test");

        assertThat(testResponse.getData()).isEqualTo("Test");
        assertNotNull(testResponse.getServerDateTime());
    }

    @Test
    @DisplayName("ApiResponse null 주입 테스트")
    void apiResponseIsNullTest() {
        assertThatNullPointerException()
            .isThrownBy(() -> new ApiResponse<>(null))
            .withMessage("data must be not null");
    }

    @Test
    @DisplayName("ApiResponse empty 주입 테스트")
    void apiResponseIsEmptyDataTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new ApiResponse<Object>(Collections.emptyList()))
            .withMessage("data must be not null");
    }

}
