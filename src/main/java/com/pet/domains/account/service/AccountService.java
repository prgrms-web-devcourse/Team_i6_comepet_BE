package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.AccountGroup;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.SignEmailRepository;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.repository.InterestAreaRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.oauth2.Oauth2User;
import com.pet.domains.auth.oauth2.ProviderType;
import com.pet.domains.auth.repository.GroupRepository;
import com.pet.domains.image.domain.Image;
import com.pet.infra.EmailMessage;
import com.pet.infra.MailSender;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    private final SignEmailRepository signEmailRepository;

    private final GroupRepository groupRepository;

    private final InterestAreaRepository interestAreaRepository;

    private final TownRepository townRepository;

    @Transactional
    public void sendEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw ExceptionMessage.DUPLICATION_EMAIL.getException();
        }
        String verifyKey = UUID.randomUUID().toString();
        mailSender.send(new EmailMessage(email, verifyKey));
        signEmailRepository.save(new SignEmail(email, verifyKey));
    }

    @Transactional
    public Long verifyEmail(String email, String key) {
        SignEmail signEmail = signEmailRepository.findByEmailAndVerifyKey(email, key)
            .filter(findSignEmail -> findSignEmail.isVerifyTime(LocalDateTime.now()))
            .orElseThrow(ExceptionMessage.INVALID_MAIL_KEY::getException);
        signEmail.successVerified();
        return signEmail.getId();
    }

    public Account login(String email, String password) {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ACCOUNT::getException);
        if (!account.isMatchPassword(passwordEncoder, password)) {
            throw ExceptionMessage.INVALID_LOGIN.getException();
        }
        return account;
    }

    public Account checkLoginAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
            ExceptionMessage.NOT_FOUND_ACCOUNT::getException
        );
    }

    @Transactional
    public Long signUp(AccountSignUpParam param) {
        compareWithPassword(param);
        checkSignEmail(param);
        return accountRepository.save(Account.builder()
            .email(param.getEmail())
            .password(passwordEncoder.encode(param.getPassword()))
            .nickname(param.getNickname())
            .group(groupRepository.findByName(AccountGroup.USER_GROUP.name())
                .orElseThrow(ExceptionMessage.NOT_FOUND_GROUP::getException))
            .build()).getId();
    }
  
    private Account checkPassword(String password, Account account) {
        if (account.isMatchPassword(passwordEncoder, password)) {
            return account;
        }
        throw ExceptionMessage.INVALID_LOGIN.getException();

    private void checkSignEmail(AccountSignUpParam param) {
        signEmailRepository.findById(param.getVerifiedId())
            .orElseThrow(ExceptionMessage.INVALID_SIGN_UP::getException)
            .isVerifyEmail(param.getEmail());
    }

    private void compareWithPassword(AccountSignUpParam param) {
        if (!StringUtils.equals(param.getPassword(), param.getPasswordCheck())) {
            throw ExceptionMessage.INVALID_SIGN_UP.getException();
        }
    }

    @Transactional
    public Account joinOath2User(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Oauth2User oauth2User = ProviderType.findProvider(provider);
        if (provider.equals(ProviderType.NAVER.getType())) {
            attributes = (Map<String, Object>) attributes.get("response");
        }
        if (provider.equals(ProviderType.KAKAO.getType())) {
            attributes = (Map<String, Object>) attributes.get("properties");
        }
        String email = oauth2User.getEmail(attributes);
        return findByEmail(provider, attributes, oauth2User, email);
    }

    private Account findByEmail(String provider, Map<String, Object> attributes, Oauth2User oauth2User, String email) {
        return accountRepository.findByEmail(email)
            .orElseGet(() -> {
                String nickname = oauth2User.getNickname(attributes);
                String profileImage = oauth2User.getProfileImage(attributes);
                Group group = groupRepository.findByName(AccountGroup.USER_GROUP.name())
                    .orElseThrow(ExceptionMessage.NOT_FOUND_GROUP::getException);
                return accountRepository.save(new Account(nickname, email, provider, new Image(profileImage), group));
            });
    }
}
