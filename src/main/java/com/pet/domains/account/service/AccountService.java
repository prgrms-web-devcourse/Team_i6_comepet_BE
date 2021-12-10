package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.AccountGroup;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.SignEmailRepository;
import com.pet.domains.auth.repository.GroupRepository;
import com.pet.infra.EmailMessage;
import com.pet.infra.MailSender;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    private final SignEmailRepository signEmailRepository;

    private final GroupRepository groupRepository;

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
    public void verifyEmail(String email, String key) {
        SignEmail signEmail = signEmailRepository.findByEmailAndVerifyKey(email, key)
            .filter(findSignEmail -> findSignEmail.isVerifyTime(LocalDateTime.now()))
            .orElseThrow(ExceptionMessage.INVALID_MAIL_KEY::getException);
        signEmail.successVerified();
    }

    public Account login(String email, String password) {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ACCOUNT::getException);
        return checkPassword(password, account);
    }

    public Account checkLoginAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
            ExceptionMessage.NOT_FOUND_ACCOUNT::getException
        );
    }

    @Transactional
    public Long signUp(AccountSignUpParam param) {
        compareWithPassword(param);
        verifyEmailAndDelete(param.getEmail());
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
    }

    private void compareWithPassword(AccountSignUpParam param) {
        if (!StringUtils.equals(param.getPassword(), param.getPasswordCheck())) {
            throw ExceptionMessage.INVALID_SIGN_UP.getException();
        }
    }

    private void verifyEmailAndDelete(String email) {
        signEmailRepository.deleteById(signEmailRepository.findByEmailAndIsCheckedTrue(email)
            .orElseThrow(ExceptionMessage.INVALID_SIGN_UP::getException).getId());
    }

}
