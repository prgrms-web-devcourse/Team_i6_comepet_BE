package com.pet.common.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtMockToken {

    /** this is mock token */
    public static final String MOCK_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
        + "eyJzdWIiOiLslYTsnbTsnKAiLCJuYW1lIjoi6riI7JqU7J287JeQIOunjOuCmOyalCIsImlh"
        + "dCI6MTUxNjIzOTAyMn0.9YOcWlxu2i3C-ETjdOLiymvwvUHnpn-lwzHwBbk0QQQ";

    private String token;

    public JwtMockToken(String header) {
        this.token = header;
    }

}