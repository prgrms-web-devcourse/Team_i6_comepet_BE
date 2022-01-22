package com.pet.domains.account.service;

import com.pet.common.exception.httpexception.NotFoundException;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.mapper.InterestAreaMapper;
import com.pet.domains.area.repository.InterestAreaRepository;
import com.pet.domains.area.repository.TownRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private InterestAreaRepository interestAreaRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private InterestAreaMapper mapper;

    @Mock
    private TownRepository townRepository;

    @Test
    @DisplayName("지역 2개 변경 성공 테스트")
    void set2AreaTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, true);
        AccountAreaUpdateParam.Area area2 = new AccountAreaUpdateParam.Area(2L, false);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1, area2), true);

        City city = City.builder().code("123").name("서울시").build();

        given(account.getId()).willReturn(1L);
        given(townRepository.findById(1L)).willReturn(Optional.of(Town.builder().name("도봉구").city(city).build()));
        given(townRepository.findById(2L)).willReturn(Optional.of(Town.builder().name("강북구").city(city).build()));
        given(mapper.toEntity(any(), any(), any())).willReturn(InterestArea.builder().build());
        given(accountRepository.save(account)).willReturn(account);

        accountService.updateArea(account, param);

        verify(interestAreaRepository, times(1)).deleteAllByAccountId(account.getId());
        verify(interestAreaRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("지역 1개 변경 성공 테스트")
    void set1AreaTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, true);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1), true);

        City city = City.builder().code("123").name("서울시").build();

        given(account.getId()).willReturn(1L);
        given(townRepository.findById(1L)).willReturn(Optional.of(Town.builder().name("도봉구").city(city).build()));
        given(mapper.toEntity(any(), any(), any())).willReturn(InterestArea.builder().build());
        given(accountRepository.save(account)).willReturn(account);

        accountService.updateArea(account, param);

        verify(interestAreaRepository, times(1)).deleteAllByAccountId(account.getId());
        verify(interestAreaRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("디폴트 지역이 2개로 요청된 경우 실패테스트")
    void set2DefaultAreaFailTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, true);
        AccountAreaUpdateParam.Area area2 = new AccountAreaUpdateParam.Area(2L, true);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1, area2), true);

        assertThrows(NotFoundException.class, () -> accountService.updateArea(account, param));
    }

    @Test
    @DisplayName("디폴트 지역 없이 요청된 경우 실패테스트")
    void setNoDefaultAreaFailTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, false);
        AccountAreaUpdateParam.Area area2 = new AccountAreaUpdateParam.Area(2L, false);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1, area2), true);

        assertThrows(NotFoundException.class, () -> accountService.updateArea(account, param));
    }

    @Test
    @DisplayName("관심 지역이 없는 경우 실패 테스트")
    void setNoAreaFailTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(Collections.emptyList(), true);

        assertThrows(NotFoundException.class, () -> accountService.updateArea(account, param));
    }

    @Test
    @DisplayName("관심 지역이 2개가 넘는 경우 실패 테스트")
    void setOverSizeAreaFailTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, true);
        AccountAreaUpdateParam.Area area2 = new AccountAreaUpdateParam.Area(2L, true);
        AccountAreaUpdateParam.Area area3 = new AccountAreaUpdateParam.Area(3L, true);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1, area2, area3), true);

        assertThrows(NotFoundException.class, () -> accountService.updateArea(account, param));
    }
}