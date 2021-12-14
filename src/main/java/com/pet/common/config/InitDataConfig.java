package com.pet.common.config;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.CityRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.post.service.ShelterApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class InitDataConfig implements ApplicationRunner {

    private final GroupPermissionRepository groupPermissionRepository;
    private final AccountRepository accountRepository;
    private final ShelterApiService shelterApiService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("runner start");
        String email = "test-user@email.com";
        Group group = new Group("USER_GROUP");
        Permission permission = new Permission("ROLE_USER");
        Account tester = new Account(
            email,
            "$2a$10$21Pd/Fr9GAN9Js6FmvahmuBMEZo73FSBUpDPXl2lTIyLWSqnQoaqi",             // user123
            "tester",
            true,
            true,
            null,
            group);

        groupPermissionRepository.save(new GroupPermission(group, permission));
        accountRepository.findByEmail(email)
            .ifPresent(account -> {
                accountRepository.deleteById(account.getId());
                log.info("임시 회원이 정상적으로 삭제되었습니다.");
            });
        accountRepository.save(tester);
    }

    public void runShelterPostSchedulerTask() {
        String sql = "INSERT INTO animal(created_at, updated_at, code, name)"
            + " VALUES (NOW(), NOW(), '417000','개'),"
            + "       (NOW(), NOW(), '422400','고양이'),"
            + "       (NOW(), NOW(), '429900','기타');";
        jdbcTemplate.execute(sql);

        log.info("saveAllAnimalKinds start..");
        shelterApiService.saveAllAnimalKinds();

        log.info("saveAllCities start..");
        shelterApiService.saveAllCities();

        log.info("saveAllTowns start..");
        shelterApiService.saveAllTowns();

        log.info("shelterPostDailyCronJob start..");
        shelterApiService.shelterPostDailyCronJob();
    }

}
