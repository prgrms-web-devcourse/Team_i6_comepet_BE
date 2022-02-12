package com.pet.domains.account.service;

import com.pet.common.jwt.Jwt;
import com.pet.common.util.Random;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.AccountGroup;
import com.pet.domains.account.domain.Email;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.dto.response.AccountLoginResult;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.EmailRepository;
import com.pet.domains.account.repository.SignEmailRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.oauth2.Oauth2User;
import com.pet.domains.auth.oauth2.ProviderType;
import com.pet.domains.auth.oauth2.RefreshToken;
import com.pet.domains.auth.repository.GroupRepository;
import com.pet.domains.auth.repository.RefreshTokenRepository;
import com.pet.domains.image.domain.Image;
import com.pet.infra.EmailMessage;
import com.pet.infra.MailSender;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.pet.common.exception.ExceptionMessage.*;
import static com.pet.common.util.Assertions.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;

    private final MailSender mailSender;

    private final SignEmailRepository signEmailRepository;

    private final GroupRepository groupRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailRepository emailRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Qualifier("refresh")
    private final Jwt refreshJwt;

    @Transactional
    public void sendEmail(String email) {
        assertThrow(accountRepository.existsByEmail(email), DUPLICATION_EMAIL.getException());
        String verifyKey = UUID.randomUUID().toString();
        mailSender.send(EmailMessage.crateVerifyEmailMessage(email, verifyKey));
        signEmailRepository.save(new SignEmail(email, verifyKey));
        // emailRepository.save(new Email(email, verifyKey));
    }

    @Transactional
    public void sendPassword(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(NOT_FOUND_ACCOUNT::getException);
        String temporaryPassword = Random.randomNewPassword();
        account.updatePassword(passwordEncoder.encode(temporaryPassword));
        mailSender.send(EmailMessage.crateNewPasswordEmailMessage(account.getEmail(), temporaryPassword));
    }

    @Transactional
    public Long verifyEmail(String email, String key) {
        SignEmail signEmail = signEmailRepository.findByEmailAndVerifyKey(email, key)
            .filter(findSignEmail -> findSignEmail.isVerifyTime(LocalDateTime.now()))
            .orElseThrow(INVALID_MAIL_KEY::getException);
        signEmail.successVerified();
        return signEmail.getId();
    }

    public Account login(String email, String password) {
        Account account = accountRepository.findByEmail(email).orElseThrow(NOT_FOUND_ACCOUNT::getException);
        assertThrow(!account.isMatchPassword(passwordEncoder, password), INVALID_LOGIN.getException());
        return account;
    }

    public Account checkLoginAccountById(Long accountId) {
        return accountRepository.findByIdAndImage(accountId).orElseThrow(NOT_FOUND_ACCOUNT::getException);
    }

    @Transactional
    public Long signUp(AccountSignUpParam param) {
        compareWithPassword(param);
        assertThrow(checkSignEmail(param), INVALID_SIGN_UP.getException());
        return accountRepository.save(Account.builder()
            .email(param.getEmail())
            .password(passwordEncoder.encode(param.getPassword()))
            .nickname(param.getNickname())
            .provider(Provider.LOCAL)
            .group(groupRepository.findByName(AccountGroup.USER_GROUP.name())
                .orElseThrow(NOT_FOUND_GROUP::getException))
            .build()).getId();
    }

    private boolean checkSignEmail(AccountSignUpParam param) {
        // signEmailRepository.findById(param.getVerifiedId())
        //     .orElseThrow(INVALID_SIGN_UP::getException)
        //     .isVerifyEmail(param.getEmail());
        return emailRepository.findById(param.getEmail()).orElseThrow(INVALID_SIGN_UP::getException)
            .isVerifyEmail(param.getEmail());
    }

    private void compareWithPassword(AccountSignUpParam param) {
        assertThrow(!StringUtils.equals(param.getPassword(), param.getPasswordCheck()), INVALID_SIGN_UP.getException());
    }

    @Transactional
    public Account joinOath2User(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Oauth2User oauth2User = ProviderType.findProvider(provider);
        String email = oauth2User.getEmail(attributes);
        return findByOauthAccount(provider, attributes, oauth2User, email);
    }

    private Account findByOauthAccount(
        String provider, Map<String, Object> attributes, Oauth2User oauth2User, String email
    ) {
        return accountRepository.findByEmail(email)
            .orElseGet(() -> {
                String nickname = oauth2User.getNickname(attributes);
                String profileImage = oauth2User.getProfileImage(attributes);
                Group group = groupRepository.findByName(AccountGroup.USER_GROUP.name())
                    .orElseThrow(NOT_FOUND_GROUP::getException);
                return accountRepository.save(Account.builder().nickname(nickname).email(email)
                    .provider(Provider.findByType(provider))
                    .profileImage(new Image(profileImage)).group(group).build());
            });
    }

    public String createRefreshToken(Long accountId, String email) {
        String refreshToken = refreshJwt.sign(Jwt.Claims.from(accountId, new String[] {"ROLE_USER"}));
        refreshTokenRepository.save(RefreshToken.builder().email(email).token(refreshToken).build());
        return refreshToken;
    }

    public Optional<Account> checkRefreshTokenAndGetAccessToken(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken)
            .map(token -> accountRepository.findByEmail(token.getEmail()))
            .orElseThrow(NullPointerException::new);
    }
}
