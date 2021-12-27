package com.pet.common.jwt;

import static org.apache.commons.lang3.ClassUtils.*;
import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.account.service.LoginService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;

    private final LoginService loginService;

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken)authentication;
        return processUserAuthentication(
            String.valueOf(jwtAuthentication.getPrincipal()), jwtAuthentication.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        return generateJwtAuthenticationToken(login(principal, credentials));
    }

    private Account login(String principal, String credentials) {
        return loginService.login(principal, credentials);
    }

    private JwtAuthenticationToken generateJwtAuthenticationToken(Account account) {
        List<GrantedAuthority> authorities = account.getGroup().getAuthorities();
        JwtAuthenticationToken authenticated = newJwtAuthenticationToken(account, authorities);
        authenticated.setDetails(account);
        return authenticated;
    }

    private JwtAuthenticationToken newJwtAuthenticationToken(Account account, List<GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(newJwtAuthentication(account, authorities), null, authorities);
    }

    private JwtAuthentication newJwtAuthentication(Account account, List<GrantedAuthority> authorities) {
        return new JwtAuthentication(getSignToken(account.getId(), authorities), account.getId());
    }

    private String getSignToken(Long accountId, List<GrantedAuthority> authorities) {
        try {
            return jwt.sign(Jwt.Claims.from(accountId, getRoles(authorities)));
        } catch (Exception e) {
            throw ExceptionMessage.INVALID_JWT.getException();
        }
    }

    private String[] getRoles(List<GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
    }

}
