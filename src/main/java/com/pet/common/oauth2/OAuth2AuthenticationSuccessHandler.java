package com.pet.common.oauth2;

import com.pet.common.jwt.Jwt;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.service.AccountService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Jwt jwt;

    private final AccountService accountService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws ServletException, IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            Account account = processUserOAuth2UserJoin((OAuth2AuthenticationToken) authentication);
            String token = generateToken(account);

            String redirectUri = determineTargetUrl(request, response, authentication);
            String targetUrl = UriComponentsBuilder
                .fromUriString(redirectUri).queryParam("token", token)
                .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

            // String loginSuccessJson =  "{\"username\": \"" + account.getId() + "\", \"token\":\""
            //     + token + "\"}";
            // setResponse(response, loginSuccessJson);

            // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            // response.setContentLength(loginSuccessJson.getBytes(StandardCharsets.UTF_8).length);
            // response.getWriter().write(loginSuccessJson);
            // response.sendRedirect("https://comepet.netlify.app/oauth/redirect?token=" + token);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    // @Override
    // protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
    //     Authentication authentication) {
    //     Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
    //         .map(Cookie::getValue);
    //     return redirectUri.orElse(getDefaultTargetUrl());
    // }

    private Account processUserOAuth2UserJoin(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        return accountService.joinOath2User(oAuth2User, registrationId);
    }

    private String generateToken(Account account) {
        return jwt.sign(Jwt.Claims.from(account.getId(), new String[] {"ROLE_USER"}));
    }

    private String generateLoginSuccessJson(Account account) {
        return "{\"username\": \"" + account.getId() + "\", \"token\":\"" + generateToken(account) + "\"}";
    }

    // protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    //     super.clearAuthenticationAttributes(request);
    //     authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    // }

    // private void setResponse(HttpServletResponse response, String loginSuccessJson) throws IOException {
    //     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    //     response.setContentLength(loginSuccessJson.getBytes(StandardCharsets.UTF_8).length);
    //     response.getWriter().write(loginSuccessJson);
    //     response.sendRedirect("http://comepet.netlify.app/oauth/" + );
    // }

}
