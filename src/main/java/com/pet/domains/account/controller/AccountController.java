package com.pet.domains.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AccountController {

    @GetMapping
    public String healthCheck() {
        return "OK";
    }

}
