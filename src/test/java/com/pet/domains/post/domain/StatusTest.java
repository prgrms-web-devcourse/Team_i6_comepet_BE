package com.pet.domains.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("게시물 상태 테스트")
class StatusTest {

    @Test
    @DisplayName("게시물 상태 getMeaning 메소드 테스트")
    void getMeaningTest() {
        assertAll(
            () -> assertThat(Status.MISSING.getMeaning()).isEqualTo("실종"),
            () -> assertThat(Status.DETECTION.getMeaning()).isEqualTo("발견"),
            () -> assertThat(Status.PROTECTION.getMeaning()).isEqualTo("보호"),
            () -> assertThat(Status.COMPLETION.getMeaning()).isEqualTo("완료")
        );
    }

}
