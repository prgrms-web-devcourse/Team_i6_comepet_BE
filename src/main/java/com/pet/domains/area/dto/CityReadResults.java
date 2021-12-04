package com.pet.domains.area.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CityReadResults {

    private final List<CityReadResults.City> cities;

    public CityReadResults(List<CityReadResults.City> cities) {
        this.cities = cities;
    }

    public static CityReadResults of(List<CityReadResults.City> cities) {
        return new CityReadResults(cities);
    }

    @Getter
    public static class City {

        private final Long id;

        private final String name;

        private final List<City.Town> towns;

        public City(Long id, String name, List<City.Town> towns) {
            this.id = id;
            this.name = name;
            this.towns = towns;
        }

        public static City of(Long id, String name, List<City.Town> towns) {
            return new City(id, name, towns);
        }

        @Getter
        public static class Town {

            private final Long id;

            private final String name;

            public Town(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public static Town of(Long id, String name) {
                return new Town(id, name);
            }

        }

    }

}
