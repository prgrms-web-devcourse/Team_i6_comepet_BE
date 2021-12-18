package com.pet.domains.post.repository.projection;


import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.ShelterPost;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShelterPostWithIsBookmark {

    private ShelterPost shelterPost;

    private Animal animal;

    private AnimalKind animalKind;

    private City city;

    private Town town;

    private boolean isBookmark;

    @QueryProjection
    public ShelterPostWithIsBookmark(ShelterPost shelterPost, Animal animal,
        AnimalKind animalKind, City city, Town town, boolean isBookmark) {
        this.shelterPost = shelterPost;
        this.animal = animal;
        this.animalKind = animalKind;
        this.city = city;
        this.town = town;
        this.isBookmark = isBookmark;
    }
}
