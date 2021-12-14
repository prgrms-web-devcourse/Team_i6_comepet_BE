package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.AccountGroup;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.SignEmailRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.mapper.InterestAreaMapper;
import com.pet.domains.area.repository.InterestAreaRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.oauth2.Oauth2User;
import com.pet.domains.auth.oauth2.ProviderType;
import com.pet.domains.auth.repository.GroupRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.service.ImageService;
import com.pet.infra.EmailMessage;
import com.pet.infra.MailSender;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final InterestAreaMapper interestAreaMapper;

    private final ImageService imageService;

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
            .provider(Provider.LOCAL)
            .group(groupRepository.findByName(AccountGroup.USER_GROUP.name())
                .orElseThrow(ExceptionMessage.NOT_FOUND_GROUP::getException))
            .build()).getId();
    }

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
            attributes = (Map<String, Object>)attributes.get("response");
        }
        if (provider.equals(ProviderType.KAKAO.getType())) {
            attributes = (Map<String, Object>)attributes.get("properties");
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
                return accountRepository.save(Account.builder().nickname(nickname).email(email)
                    .provider(Provider.findByType(provider))
                    .profileImage(new Image(profileImage)).group(group).build());
            });
    }

    @Transactional
    public void updateArea(Account account, AccountAreaUpdateParam accountAreaUpdateParam) {
        if (!accountAreaUpdateParam.getAreas().isEmpty()) {
            interestAreaRepository.deleteAllByAccountId(account.getId());
        }

        interestAreaRepository.saveAll(accountAreaUpdateParam.getAreas().stream()
            .map(area -> {
                Long townId = area.getTownId();
                return InterestArea.builder()
                    .account(account)
                    .selected(area.isDefaultArea())
                    .town(townRepository.getById(townId))
                    .build();
            })
            .distinct()
            .limit(2)
            .collect(Collectors.toList()));

        account.updateNotification(accountAreaUpdateParam.isNotification());
        accountRepository.save(account);
    }

    @Transactional
    public void updateAccount(Account account, AccountUpdateParam accountUpdateParam) {
        if (!StringUtils.equals(accountUpdateParam.getNewPassword(), accountUpdateParam.getNewPasswordCheck())) {
            throw ExceptionMessage.INVALID_PASSWORD.getException();
        }
        account.updateProfile(
            accountUpdateParam.getNickname(),
            passwordEncoder.encode(accountUpdateParam.getNewPassword())
        );
        accountRepository.save(account);
    }

    public AccountAreaReadResults getInterestArea(Account account) {
        List<InterestArea> interestAreas = interestAreaRepository.findByAccountId(account.getId());
        return AccountAreaReadResults.of(interestAreas.stream()
            .map(interestArea -> {
                Town town = interestArea.getTown();
                City city = town.getCity();
                return interestAreaMapper.toAreaResult(city, town, interestArea.isSelected());
            })
            .collect(Collectors.toList()));
    }

    @Transactional
    public void updateAccountImage(Account account, MultipartFile accountProfile) {
        account.updateProfileImage(imageService.createImage(accountProfile));
        accountRepository.save(account);
    }

}
