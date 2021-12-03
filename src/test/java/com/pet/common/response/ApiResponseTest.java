package com.pet.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Api Response 테스트")
class ApiResponseTest {

    @Test
    @DisplayName("ApiResponse 생성 테스트")
    void newApiResponse() {
        ApiResponse<String> tester = ApiResponse.ok("Tester");

        assertThat(tester.getData()).isEqualTo("Tester");
        assertNotNull(tester.getServerDateTime());
    }

    @Test
    @DisplayName("ApiResponse null 주입 테스트")
    void apiResponseIsFinalObject() {
        assertThatNullPointerException()
            .isThrownBy(() -> new ApiResponse(null))
            .withMessage("data must be not null");
    }

}
