package com.pet.domains.animal.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnimalReadResults {

    private List<Animal> animals;

    private AnimalReadResults(List<AnimalReadResults.Animal> animals) {
        this.animals = animals;
    }

    public static AnimalReadResults of(List<AnimalReadResults.Animal> animals) {
        return new AnimalReadResults(animals);
    }

    @Getter
    @NoArgsConstructor
    public static class Animal {

        private Long id;

        private String name;

        private List<Animal.AnimalKind> kinds;

        @Builder
        public Animal(Long id, String name, List<Animal.AnimalKind> kinds) {
            this.id = id;
            this.name = name;
            this.kinds = kinds;
        }

        @Getter
        @NoArgsConstructor
        public static class AnimalKind {

            private Long id;

            private String name;

            @Builder
            public AnimalKind(Long id, String name) {
                this.id = id;
                this.name = name;
            }
        }
    }

    @Override
    public String toString() {
        return "animals";
    }
}
