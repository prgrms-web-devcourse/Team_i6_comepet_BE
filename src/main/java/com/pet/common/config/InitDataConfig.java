package com.pet.common.config;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.SignStatus;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("local")
public class InitDataConfig implements ApplicationRunner {

    private final GroupPermissionRepository groupPermissionRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String email = "test-user@email.com";
        Group group = new Group("USER_GROUP");
        Permission permission = new Permission("ROLE_USER");
        Account tester = new Account(
            email,
            "$2a$10$21Pd/Fr9GAN9Js6FmvahmuBMEZo73FSBUpDPXl2lTIyLWSqnQoaqi",             // user123
            "tester",
            true,
            true,
            SignStatus.SUCCESS,
            null,
            group);

        accountRepository.findByEmail(email)
            .ifPresent(account -> {
                accountRepository.deleteById(account.getId());
                log.info("임시 회원이 정상적으로 삭제되었습니다.");
            });

        groupPermissionRepository.save(new GroupPermission(group, permission));
        accountRepository.save(tester);
    }

}
