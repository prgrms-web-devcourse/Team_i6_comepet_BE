package com.pet.domains.auth.oauth2;

import java.util.Map;

public class GoogleUser implements Oauth2User {

    private final String nickname;

    private final String email;

    private final String profileImage;

    public GoogleUser(String nickname, String email, String profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }

    @Override
    public String getNickname(Map<String, Object> attributes) {
        return (String) attributes.get(nickname);
    }

    @Override
    public String getEmail(Map<String, Object> attributes) {
        return (String) attributes.get(email);
    }

    @Override
    public String getProfileImage(Map<String, Object> attributes) {
        return (String) attributes.get(profileImage);
    }
}
