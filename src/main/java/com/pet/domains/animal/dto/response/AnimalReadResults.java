package com.pet.domains.animal.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AnimalReadResults {

    private final List<Animal> animals;

    private AnimalReadResults(List<AnimalReadResults.Animal> animals) {
        this.animals = animals;
    }

    public static AnimalReadResults of(List<AnimalReadResults.Animal> animals) {
        return new AnimalReadResults(animals);
    }

    @Getter
    public static class Animal {

        private final Long id;

        private final String name;

        private final List<Animal.AnimalKind> kinds;

        @Builder
        public Animal(Long id, String name, List<Animal.AnimalKind> kinds) {
            this.id = id;
            this.name = name;
            this.kinds = kinds;
        }

        @Getter
        public static class AnimalKind {

            private final Long id;

            private final String name;

            @Builder
            public AnimalKind(Long id, String name) {
                this.id = id;
                this.name = name;
            }
        }
    }
}
