package com.pet.domains.auth.oauth2;

import java.util.Map;

public class KakaoUser implements Oauth2User {

    private static final String KAKAO_OAUTH = "properties";

    private final String nickname;

    private final String email;

    private final String profileImage;

    public KakaoUser(String nickname, String email, String profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }

    @Override
    public String getNickname(Map<String, Object> attributes) {
        return (String)getKakaoAttributes(attributes).get(nickname);
    }

    @Override
    public String getEmail(Map<String, Object> attributes) {
        return (String)getKakaoAttributes(attributes).get(email);
    }

    @Override
    public String getProfileImage(Map<String, Object> attributes) {
        return (String)getKakaoAttributes(attributes).get(profileImage);
    }

    private Map<String, Object> getKakaoAttributes(Map<String, Object> attributes) {
        return (Map<String, Object>)attributes.get(KAKAO_OAUTH);
    }
}
