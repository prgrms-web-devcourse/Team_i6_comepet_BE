package com.pet.domains.auth.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth-user")
@Slf4j
public class AuthController {

    @GetMapping
    public ApiResponse<Map<String, Long>> checkToken(@LoginAccount Account account) {
        return ApiResponse.ok(Map.of("id", account.getId()));
    }

}
