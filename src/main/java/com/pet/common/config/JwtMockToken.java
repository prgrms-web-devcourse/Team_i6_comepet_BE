package com.pet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtMockToken {

    @Bean
    public String mockToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
            + "eyJzdWIiOiLslYTsnbTsnKAiLCJuYW1lIjoi6r"
            + "iI7JqU7J287JeQIOunjOuCmOyalCIsImlhdCI6"
            + "MTUxNjIzOTAyMn0.9YOcWlxu2i3C-ETjdOLiym"
            + "vwvUHnpn-lwzHwBbk0QQQ";
    }

}
