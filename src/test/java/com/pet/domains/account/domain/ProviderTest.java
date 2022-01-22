package com.pet.domains.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

@DisplayName("로그인 Provider 테스트")
class ProviderTest {

    @Test
    @DisplayName("local provider 조회 테스트")
    void findLocalProviderTest() {
        Provider provider = Provider.findByType("local");
        assertThat(provider.getType()).isEqualTo("local");
    }

    @Test
    @DisplayName("google provider 조회 테스트")
    void findGoogleProviderTest() {
        Provider provider = Provider.findByType("google");
        assertThat(provider.getType()).isEqualTo("google");
    }

    @Test
    @DisplayName("kakao provider 조회 테스트")
    void findKakaoProviderTest() {
        Provider provider = Provider.findByType("kakao");
        assertThat(provider.getType()).isEqualTo("kakao");
    }

    @Test
    @DisplayName("naver provider 조회 테스트")
    void findNaverProviderTest() {
        Provider provider = Provider.findByType("naver");
        assertThat(provider.getType()).isEqualTo("naver");
    }
}