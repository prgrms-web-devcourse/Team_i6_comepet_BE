package com.pet.domains.area.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CityReadResults {

    private List<CityReadResults.City> cities;

    public CityReadResults(List<CityReadResults.City> cities) {
        this.cities = cities;
    }

    public static CityReadResults of(List<CityReadResults.City> cities) {
        return new CityReadResults(cities);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class City {

        private Long id;

        private String name;

        private List<City.Town> towns;

        public City(Long id, String name, List<City.Town> towns) {
            this.id = id;
            this.name = name;
            this.towns = towns;
        }

        public static City of(Long id, String name, List<City.Town> towns) {
            return new City(id, name, towns);
        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Town {

            private Long id;

            private String name;

            public Town(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public static Town of(Long id, String name) {
                return new Town(id, name);
            }

        }

    }
    @Override
    public String toString() {
        return "cities";
    }

}
