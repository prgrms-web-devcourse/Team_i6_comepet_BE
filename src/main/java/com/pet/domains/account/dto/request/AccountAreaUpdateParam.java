package com.pet.domains.account.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountAreaUpdateParam {

    private List<AccountAreaUpdateParam.Area> areas;

    private boolean notification;

    public AccountAreaUpdateParam(List<AccountAreaUpdateParam.Area> areas, boolean notification) {
        this.areas = areas;
        this.notification = notification;
    }

    public static AccountAreaUpdateParam of(List<AccountAreaUpdateParam.Area> areas, boolean notification) {
        return new AccountAreaUpdateParam(areas, notification);
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

        public static AccountAreaUpdateParam.Area of(Long cityId, Long townId, boolean defaultArea) {
            return new AccountAreaUpdateParam.Area(cityId, townId, defaultArea);
        }
    }

}
