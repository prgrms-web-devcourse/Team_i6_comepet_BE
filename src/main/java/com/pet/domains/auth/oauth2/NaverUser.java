package com.pet.domains.auth.oauth2;

import java.util.Map;

public class NaverUser implements Oauth2User {

    private static final String NAVER_OAUTH = "response";

    private final String nickname;

    private final String email;

    private final String profileImage;

    public NaverUser(String nickname, String email, String profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }

    @Override
    public String getNickname(Map<String, Object> attributes) {
        return (String)getNaverAttributes(attributes).get(nickname);
    }

    @Override
    public String getEmail(Map<String, Object> attributes) {
        return (String)getNaverAttributes(attributes).get(email);
    }

    @Override
    public String getProfileImage(Map<String, Object> attributes) {
        return (String)getNaverAttributes(attributes).get(profileImage);
    }

    private Map<String, Object> getNaverAttributes(Map<String, Object> attributes) {
        return (Map<String, Object>)attributes.get(NAVER_OAUTH);
    }
}
