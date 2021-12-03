package com.pet.domains.animal.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class AnimalReadResults {

    private final List<Animal> animals;

    public AnimalReadResults(List<Animal> animals) {
        this.animals = animals;
    }

    public static AnimalReadResults of(List<Animal> animals) {
        return new AnimalReadResults(animals);
    }

    @Getter
    public static class Animal {

        private final Long id;

        private final String name;

        private final List<AnimalKind> kinds;

        public Animal(Long id, String name, List<AnimalKind> kinds) {
            this.id = id;
            this.name = name;
            this.kinds = kinds;
        }

        public static Animal of(Long id, String name, List<AnimalKind> kinds) {
            return new Animal(id, name, kinds);
        }

        @Getter
        public static class AnimalKind {

            private final Long id;

            private final String name;

            public AnimalKind(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public static AnimalKind of(Long id, String name) {
                return new AnimalKind(id, name);
            }
        }
    }
}
