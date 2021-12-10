package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.SignEmailRepository;
import com.pet.infra.EmailMessage;
import com.pet.infra.MailSender;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void checkDuplicationEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw ExceptionMessage.DUPLICATION_EMAIL.getException();
        }

        String verifyKey = UUID.randomUUID().toString();
        mailSender.send(new EmailMessage(email, verifyKey));
        signEmailRepository.save(new SignEmail(email, verifyKey));
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
    public void signUp(String email, String password) {
        accountRepository.save(new Account(email, passwordEncoder.encode(password)));
    }

    private Account checkPassword(String password, Account account) {
        if (account.isMatchPassword(passwordEncoder, password)) {
            return account;
        }
        throw ExceptionMessage.INVALID_LOGIN.getException();
    }
}
