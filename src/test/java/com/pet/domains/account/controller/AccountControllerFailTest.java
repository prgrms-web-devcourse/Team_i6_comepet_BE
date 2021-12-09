package com.pet.domains.account.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.dto.request.AccountEmailCheck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("회원 컨트롤러 실패 테스트")
public class AccountControllerFailTest extends AccountControllerTest {

    @Test
    @DisplayName("이메일 중복 요청 실패 테스트")
    void emailVerifyFailTest() throws Exception {
        // given
        String email = "tester@email.com";
        AccountEmailCheck param = new AccountEmailCheck(email);
        willThrow(ExceptionMessage.DUPLICATION_EMAIL.getException()).given(accountService).checkDuplicationEmail(email);
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/verify-email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

}
