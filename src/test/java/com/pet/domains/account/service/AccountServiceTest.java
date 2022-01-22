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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
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
        Town town1 = Town.builder().name("도봉구").city(city).build();
        Town town2 = Town.builder().name("강북구").city(city).build();

        InterestArea defaultArea = InterestArea.builder().account(account).town(town1).build();
        InterestArea secondArea = InterestArea.builder().account(account).town(town1).build();

        given(townRepository.findById(1L)).willReturn(Optional.of(town1));
        given(townRepository.findById(2L)).willReturn(Optional.of(town2));
        given(mapper.toEntity(account, area1, town1)).willReturn(defaultArea);
        given(mapper.toEntity(account, area2, town2)).willReturn(secondArea);
        given(accountRepository.save(account)).willReturn(account);

        accountService.updateArea(account, param);

        verify(interestAreaRepository, times(1)).deleteAllByAccountId(account.getId());
        verify(interestAreaRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("지역 1개 변경 성공 테스트")
    void set1AreaTest() {
        Account account = mock(Account.class);
        AccountAreaUpdateParam.Area area1 = new AccountAreaUpdateParam.Area(1L, true);
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(List.of(area1), true);

        City city = City.builder().code("123").name("서울시").build();
        Town town = Town.builder().name("도봉구").city(city).build();
        InterestArea interestArea = InterestArea.builder().account(account).town(town).build();

        given(account.getId()).willReturn(1L);
        given(townRepository.findById(1L)).willReturn(Optional.of(town));
        given(mapper.toEntity(any(), any(), any())).willReturn(interestArea);
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

    @Test
    @DisplayName("관심 지역이 2개였을 때 디폴트 지역을 삭제한 경우 남은 관심지역이 디폴트 지역으로 변경되는지 테스트")
    void deleteDefaultArea() {
        Account account = mock(Account.class);
        City city = City.builder().code("123").name("서울시").build();
        Town town = Town.builder().name("강북구").city(city).build();

        List<InterestArea> areas = new ArrayList<>();
        InterestArea defaultArea = mock(InterestArea.class);
        areas.add(defaultArea);
        areas.add(InterestArea.builder().account(account).town(town).selected(false).build());

        given(account.getId()).willReturn(1L);
        given(defaultArea.getId()).willReturn(1L);
        given(interestAreaRepository.findByAccountId(account.getId())).willReturn(areas);

        accountService.deleteArea(account, 1L);

        verify(interestAreaRepository, times(1)).delete(defaultArea);

        assertThat(areas.size()).isEqualTo(1);
        assertThat(areas.get(0).isSelected()).isTrue();
    }
}