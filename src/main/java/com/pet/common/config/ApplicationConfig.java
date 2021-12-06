package com.pet.common.config;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.SignStatus;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.image.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationConfig implements ApplicationRunner {

    private final GroupPermissionRepository groupPermissionRepository;
    private final AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Group group = new Group("USER_GROUP");
        Permission permission = new Permission("ROLE_USER");
        Account tester = new Account(
            "test-user@email.com",
            "1234",
            "tester",
            true,
            true,
            SignStatus.SUCCESS,
            null,
            group);

        // groupRepository.save(group);
        // permissionRepository.save(permission);

        groupPermissionRepository.save(new GroupPermission(group, permission));
        accountRepository.save(tester);

    }

}
