package com.pet.domains.account;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import com.pet.common.jwt.JwtAuthenticationProvider;
import com.pet.common.jwt.JwtAuthenticationToken;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.account.service.LoginService;
import com.pet.domains.auth.domain.Group;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private static final String ROLE_USER = "ROLE_USER";

    private final JwtAuthenticationProvider provider;

    private final LoginService loginService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String email = "tester@email.com";
        String password = "user123";
        String nickname = withAccount.value();
        Group group = mock(Group.class);
        Account account = givenAccount(email, nickname, group);

        given(loginService.login(email, password)).willReturn(account);
        given(loginService.checkLoginAccountById(anyLong())).willReturn(account);
        given(group.getAuthorities()).willReturn(List.of((GrantedAuthority)() -> ROLE_USER));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(provider.authenticate(new JwtAuthenticationToken(email, password)));
        return context;
    }

    private Account givenAccount(String email, String nickname, Group group) {
        return new Account(
            9127364171L,
            email,
            "$2a$10$21Pd/Fr9GAN9Js6FmvahmuBMEZo73FSBUpDPXl2lTIyLWSqnQoaqi",
            nickname,
            true,
            true,
            null,
            group,
            Provider.LOCAL);
    }

}
