package com.pet.domains.account.service;

import static com.pet.common.exception.ExceptionMessage.*;
import static com.pet.common.util.Assertions.*;
import com.pet.common.util.PasswordUtil;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.account.dto.response.AccountMissingPostPageResults;
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.account.mapper.AccountMapper;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.mapper.InterestAreaMapper;
import com.pet.domains.area.repository.InterestAreaRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.repository.MissingPostRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {
    private static final int INTEREST_AREA_MAX_COUNT = 2;
    private static final int DEFAULT_AREA_MAX_COUNT = 1;
    private static final int DUPLICATE = 1;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final InterestAreaRepository interestAreaRepository;

    private final TownRepository townRepository;

    private final InterestAreaMapper interestAreaMapper;

    private final MissingPostRepository missingPostRepository;

    private final ImageService imageService;

    private final AccountMapper accountMapper;

    @Transactional
    public void updateArea(Account account, AccountAreaUpdateParam accountAreaUpdateParam) {
        interestAreaRepository.deleteAllByAccountId(account.getId());

        List<AccountAreaUpdateParam.Area> areas = accountAreaUpdateParam.getAreas();
        assertThrow(areas.isEmpty(), INVALID_INTEREST_AREA.getException());
        assertThrow(areas.size() > INTEREST_AREA_MAX_COUNT, INVALID_INTEREST_AREA.getException());

        if (areas.size() == INTEREST_AREA_MAX_COUNT) {
            validateArea(areas);
        }

        Queue<AccountAreaUpdateParam.Area> queue = new LinkedList<>(areas);
        while (!queue.isEmpty()) {
            AccountAreaUpdateParam.Area area = queue.poll();
            InterestArea interestArea = interestAreaMapper.toEntity(account, area, findTownByArea(area));
            interestAreaRepository.save(interestArea);
        }

        account.updateNotification(accountAreaUpdateParam.isNotification());
        accountRepository.save(account);
    }

    private void validateArea(List<AccountAreaUpdateParam.Area> areas) {
        long defaultAreaCount = areas.stream().filter(AccountAreaUpdateParam.Area::isDefaultArea).count();
        assertThrow(defaultAreaCount != DEFAULT_AREA_MAX_COUNT, INVALID_INTEREST_AREA.getException());
        long townIdCount = areas.stream().map(AccountAreaUpdateParam.Area::getTownId).distinct().count();
        assertThrow(townIdCount == DUPLICATE, INVALID_INTEREST_AREA.getException());
    }

    private Town findTownByArea(AccountAreaUpdateParam.Area area) {
        return townRepository.findById(area.getTownId())
            .orElseThrow(NOT_FOUND_TOWN::getException);
    }

    @Transactional
    public void updateAccount(Account account, AccountUpdateParam accountUpdateParam, MultipartFile accountImage) {
        validatePassword(accountUpdateParam.getNewPassword(), accountUpdateParam.getNewPasswordCheck());
        account.updateProfile(
            accountUpdateParam.getNickname(),
            encodePassword(accountUpdateParam.getNewPassword()),
            checkNullImage(accountImage)
        );
        accountRepository.save(account);
    }

    private Image checkNullImage(MultipartFile accountImage) {
        if (Objects.nonNull(accountImage)) {
            return imageService.createImage(accountImage);
        }
        return null;
    }

    private String encodePassword(String password) {
        if (StringUtils.isNotBlank(password)) {
            return passwordEncoder.encode(password);
        }
        return null;
    }

    private void validatePassword(String password, String passwordCheck) {
        assertThrow(!StringUtils.equals(password, passwordCheck), INVALID_PASSWORD.getException());
        assertThrow(StringUtils.isNotBlank(password) && !PasswordUtil.match(password),
            INVALID_PASSWORD_REGEX.getException());
    }

    public AccountAreaReadResults getInterestArea(Account account) {
        return AccountAreaReadResults.of(interestAreaRepository.findByAccountId(account.getId()).stream()
            .map(interestArea -> interestAreaMapper.toAreaResult(interestArea, account.isCheckedArea()))
            .collect(Collectors.toList()), account.isNotification());
    }

    public AccountReadResult getAccount(Account account) {
        return accountMapper.toReadResult(accountRepository.findByIdAndImage(account.getId())
            .orElseThrow(NOT_FOUND_ACCOUNT::getException));
    }

    @Transactional
    public void deleteArea(Account account, Long areaId) {
        List<InterestArea> interestAreas = interestAreaRepository.findByAccountId(account.getId());
        InterestArea deletedArea = interestAreas.stream()
            .filter(interestArea -> interestArea.getId().equals(areaId))
            .findAny()
            .orElseThrow(NOT_FOUND_INTEREST_AREA::getException);
        interestAreas.remove(deletedArea);

        interestAreaRepository.delete(deletedArea);

        if (interestAreas.size() == 1) {
            InterestArea interestArea = interestAreas.get(0);
            interestArea.checkSelect();
        }

    }

    public AccountMissingPostPageResults getAccountPosts(Long id, Pageable pageable) {
        Page<MissingPost> missingPosts = missingPostRepository.findByAccountId(id, pageable);
        return AccountMissingPostPageResults.of(missingPosts.stream()
                .map(accountMapper::toAccountMissingPostPageResults)
                .collect(Collectors.toList()),
            missingPosts.getTotalElements(),
            missingPosts.isLast(),
            missingPosts.getSize());
    }

    @Transactional
    public void deleteAccount(Account account) {
        missingPostRepository.deleteAllByAccount(account);
        accountRepository.delete(account);
    }
}
