package com.pet.common.jwt;

import static org.apache.commons.lang3.ClassUtils.*;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;
    private final AccountService accountService;

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
        try {
            return createJwtAuthenticationToken(principal, credentials);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private JwtAuthenticationToken createJwtAuthenticationToken(String principal, String credentials) {
        Account account = accountService.login(principal, credentials);
        List<GrantedAuthority> authorities = account.getGroup().getAuthorities();
        String token = getToken(account.getId(), authorities);
        JwtAuthenticationToken authenticated = new JwtAuthenticationToken(
            new JwtAuthentication(token, account.getId()), null, authorities
        );
        authenticated.setDetails(account);
        return authenticated;
    }

    private String getToken(Long accountId, List<GrantedAuthority> authorities) {
        return jwt.sign(Jwt.Claims.from(accountId, getRoles(authorities)));
    }

    private String[] getRoles(List<GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
    }

}
