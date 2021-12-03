package com.pet.domains.area.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CityReadResults {

    private final List<City> cities;

    public CityReadResults(List<City> cities) {
        this.cities = cities;
    }

    public static CityReadResults of(List<City> cities) {
        return new CityReadResults(cities);
    }

    @Getter
    public static class City {

        private Long id;

        private String name;

        private List<Town> towns;

        public City(Long id, String name, List<Town> towns) {
            this.id = id;
            this.name = name;
            this.towns = towns;
        }

        public static City of(Long id, String name, List<Town> towns) {
            return new City(id, name, towns);
        }

        @Getter
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

}
