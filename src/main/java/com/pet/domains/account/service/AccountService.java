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
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.account.mapper.AccountMapper;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.SignEmailRepository;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private final AccountMapper accountMapper;

    @Transactional
    public void sendEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw ExceptionMessage.DUPLICATION_EMAIL.getException();
        }
        String verifyKey = UUID.randomUUID().toString();
        mailSender.send(EmailMessage.crateVerifyEmailMessage(email, verifyKey));
        signEmailRepository.save(new SignEmail(email, verifyKey));
    }

    @Transactional
    public void sendPassword(Long accountId) {
        Account account =
            accountRepository.findById(accountId).orElseThrow(ExceptionMessage.NOT_FOUND_ACCOUNT::getException);
        String temporaryPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        account.updatePassword(passwordEncoder.encode(temporaryPassword));
        mailSender.send(EmailMessage.crateNewPasswordEmailMessage(account.getEmail(), temporaryPassword));
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
        interestAreaRepository.deleteAllByAccountId(account.getId());

        List<AccountAreaUpdateParam.Area> areas = accountAreaUpdateParam.getAreas();
        if (areas.isEmpty()) {
            log.debug("관심 지역을 0개 입력했습니다.");
            throw ExceptionMessage.INVALID_INTEREST_AREA.getException();
        }

        if (areas.size() > 2) {
            log.debug("관심 지역이 2개를 넘었습니다.");
            throw ExceptionMessage.INVALID_INTEREST_AREA.getException();
        }

        // 관심 지역이 1개인 경우 디폴트 지역으로 저장
        if (areas.size() == 1) {
            AccountAreaUpdateParam.Area defaultArea = accountAreaUpdateParam.getAreas().get(0);
            InterestArea interestArea = interestAreaMapper.toEntity(account, defaultArea, findTownByArea(defaultArea));
            interestArea.checkSelect();
        }

        if (areas.size() == 2) {
            long defaultAreaCount = areas.stream().filter(AccountAreaUpdateParam.Area::isDefaultArea).count();
            if (defaultAreaCount == 2) {
                log.debug("디폴트 지역이 2개입니다.");
                throw ExceptionMessage.INVALID_INTEREST_AREA.getException();
            }

            long townIdCount = areas.stream().map(AccountAreaUpdateParam.Area::getTownId).distinct().count();
            if (townIdCount == 2) {
                log.debug("타운 아이디가 같습니다.");
                throw ExceptionMessage.INVALID_INTEREST_AREA.getException();
            }

            interestAreaRepository.saveAll(
                areas.stream()
                    .map(area -> interestAreaMapper.toEntity(account, area, findTownByArea(area)))
                    .collect(Collectors.toList()));
        }

        account.updateNotification(accountAreaUpdateParam.isNotification());
        accountRepository.save(account);
    }

    private Town findTownByArea(AccountAreaUpdateParam.Area area) {
        return townRepository.findById(area.getTownId())
            .orElseThrow(ExceptionMessage.NOT_FOUND_TOWN::getException);
    }

    @Transactional
    public void updateAccount(Account account, AccountUpdateParam accountUpdateParam, MultipartFile accountImage) {
        if (!StringUtils.equals(accountUpdateParam.getNewPassword(), accountUpdateParam.getNewPasswordCheck())) {
            log.debug("새로운 비밀번호의 입력값이 다릅니다.");
            throw ExceptionMessage.INVALID_PASSWORD.getException();
        }
        validatePassword(accountUpdateParam.getNewPassword());
        account.updateProfile(
            accountUpdateParam.getNickname(),
            passwordEncoder.encode(accountUpdateParam.getNewPassword()),
            imageService.createImage(accountImage)
        );
        accountRepository.save(account);
    }

    private void validatePassword(String newPassword) {
        if (StringUtils.isNotBlank(newPassword)) {
            if (!newPassword
                .matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$")) {
                throw ExceptionMessage.INVALID_PASSWORD_REGEX.getException();
            }
        }
    }

    public AccountAreaReadResults getInterestArea(Account account) {
        List<InterestArea> interestAreas = interestAreaRepository.findByAccountId(account.getId());
        return AccountAreaReadResults.of(interestAreas.stream()
            .map(interestArea -> interestAreaMapper.toAreaResult(interestArea, account.isCheckedArea()))
            .collect(Collectors.toList()), account.isNotification());
    }

    public AccountReadResult getAccount(Account account) {
        return accountMapper.toReadResult(
            accountRepository.findByIdAndImage(account.getId())
                .orElseThrow(ExceptionMessage.NOT_FOUND_ACCOUNT::getException));
    }

    @Transactional
    public void deleteArea(Account account, Long areaId) {
        List<InterestArea> interestAreas = interestAreaRepository.findByAccountId(account.getId());
        interestAreas.remove(interestAreas.stream()
            .filter(interestArea -> interestArea.getId().equals(areaId))
            .findAny()
            .orElseThrow(ExceptionMessage.NOT_FOUND_INTEREST_AREA::getException));

        // 관심 지역을 하나 삭제하고 하나가 더 남아있는 경우 selected == false -> true
        if (!interestAreas.isEmpty()) {
            interestAreas.forEach(InterestArea::checkSelect);
        }

    }

}
