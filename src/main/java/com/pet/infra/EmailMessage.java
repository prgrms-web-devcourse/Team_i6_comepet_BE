package com.pet.infra;

import lombok.Getter;

@Getter
public class EmailMessage {

    private static final String COMPET_SUBJECT = "컴팻 이메일 인증 요청입니다.";

    private String to;

    private String subject;

    private String message;

    public EmailMessage(String to, String message) {
        this.to = to;
        this.subject = COMPET_SUBJECT;
        this.message = message;
    }

}
