package com.pet.domains.account.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class AccountAreaReadResults {

    private List<AccountAreaReadResults.Area> areas;

    public AccountAreaReadResults(List<AccountAreaReadResults.Area> areas) {
        this.areas = areas;
    }

    public static AccountAreaReadResults of(List<AccountAreaReadResults.Area> areas) {
        return new AccountAreaReadResults(areas);
    }

    @Getter
    public static class Area {

        private Long cityId;

        private String cityName;

        private Long townId;

        private String townName;

        private boolean defaultArea;

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
