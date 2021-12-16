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

        private final Long id;

        private final Long cityId;

        private final String cityName;

        private final Long townId;

        private final String townName;

        private final boolean defaultArea;

        public Area(Long id, Long cityId, String cityName, Long townId, String townName, boolean defaultArea) {
            this.id = id;
            this.cityId = cityId;
            this.cityName = cityName;
            this.townId = townId;
            this.townName = townName;
            this.defaultArea = defaultArea;
        }

        public static Area of(
            Long id, Long cityId, String cityName, Long townId, String townName, boolean defaultArea
        ) {
            return new Area(id, cityId, cityName, townId, townName, defaultArea);
        }
    }

}
