package com.pet.domains.auth.controller.argumentresolver;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.account.service.AccountService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class JwtAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccountService accountService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginAccount.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validateAuthentication(authentication);

        if (isAnonymousAuthentication(authentication)) {
            return null;
        }
        return accountService.checkLoginAccountById(getJwtAuthentication(authentication).getAccountId());
    }

    private JwtAuthentication getJwtAuthentication(Authentication authentication) {
        return (JwtAuthentication)authentication.getPrincipal();
    }

    private void validateAuthentication(Authentication authentication) {
        if (Objects.isNull(authentication)) {
            throw ExceptionMessage.INVALID_JWT.getException();
        }
    }

    private boolean isAnonymousAuthentication(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

}
