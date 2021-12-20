package com.pet.infra;

import lombok.Getter;

@Getter
public class EmailMessage {

    private static final String COMPET_SUBJECT = "컴팻 이메일 인증 요청입니다.";
    private static final String COMPET_PASSWORD = "컴팻 임시 비밀번호입니다.";

    private String to;

    private String subject;

    private String message;

    public EmailMessage(String to, String message, String subject) {
        this.to = to;
        this.message = message;
        this.subject = subject;
    }

    public static EmailMessage crateVerifyEmailMessage(String to, String message) {
        return new EmailMessage(to, message, COMPET_SUBJECT);
    }

    public static EmailMessage crateNewPasswordEmailMessage(String to, String message) {
        return new EmailMessage(to, message, COMPET_PASSWORD);
    }

}
