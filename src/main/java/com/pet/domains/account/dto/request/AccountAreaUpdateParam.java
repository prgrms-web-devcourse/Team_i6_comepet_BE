package com.pet.domains.account.dto.request;

import java.util.List;
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

        private Long cityId;

        private Long townId;

        private boolean defaultArea;

        public Area(Long cityId, Long townId, boolean defaultArea) {
            this.cityId = cityId;
            this.townId = townId;
            this.defaultArea = defaultArea;
        }

    }

}