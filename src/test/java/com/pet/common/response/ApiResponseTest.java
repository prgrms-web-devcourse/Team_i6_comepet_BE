package com.pet.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
            .isThrownBy(() -> new ApiResponse(null))
            .withMessage("data must be not null");
    }

    @Test
    @DisplayName("ApiResponse empty 주입 테스트")
    void apiResponseIsEmptyDataTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new ApiResponse(Collections.emptyList()))
            .withMessage("data must be not null");
    }

}
