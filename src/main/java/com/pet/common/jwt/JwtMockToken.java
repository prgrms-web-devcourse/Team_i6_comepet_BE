package com.pet.common.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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

    public boolean isVerify() {
        ObjectUtils.requireNonEmpty(token, "token is empty");
        return StringUtils.equals(MOCK_TOKEN, token);
    }

}
