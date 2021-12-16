package com.pet.domains.account.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class AccountAreaReadResults {

    private final List<AccountAreaReadResults.Area> areas;

    private final boolean checked;

    public AccountAreaReadResults(List<AccountAreaReadResults.Area> areas, boolean checked) {
        this.areas = areas;
        this.checked = checked;
    }

    public static AccountAreaReadResults of(List<AccountAreaReadResults.Area> areas, boolean checked) {
        return new AccountAreaReadResults(areas, checked);
    }

    @Getter
    public static class Area {

        private final Long cityId;

        private final String cityName;

        private final Long townId;

        private final String townName;

        private final boolean defaultArea;

        public Area(Long cityId, String cityName, Long townId, String townName, boolean defaultArea) {
            this.cityId = cityId;
            this.cityName = cityName;
            this.townId = townId;
            this.townName = townName;
            this.defaultArea = defaultArea;
        }

        public static Area of(Long cityId, String cityName, Long townId, String townName, boolean defaultArea) {
            return new Area(cityId, cityName, townId, townName, defaultArea);
        }
    }

}
