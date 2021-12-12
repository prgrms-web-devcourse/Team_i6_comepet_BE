package com.pet.domains.account.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountAreaUpdateParam {

    private List<AccountAreaUpdateParam.Area> areas;

    private boolean notification;

    public AccountAreaUpdateParam(List<AccountAreaUpdateParam.Area> areas, boolean notification) {
        this.areas = areas;
        this.notification = notification;
    }

    @Getter
    public static class Area {

        @NotBlank(message = "'시/군/구'를 입력해주세요.")
        private Long townId;

        private boolean defaultArea;

        public Area(Long townId, boolean defaultArea) {
            this.townId = townId;
            this.defaultArea = defaultArea;
        }

    }

}
