package com.pet.domains.auth.oauth2;

import java.util.Map;

public interface Oauth2User {

    String getNickname(Map<String, Object> attributes);

    String getEmail(Map<String, Object> attributes);

    String getProfileImage(Map<String, Object> attributes);

}
