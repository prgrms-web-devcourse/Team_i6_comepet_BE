package com.pet.domains.animal.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.mapper.AnimalMapper;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalMapper animalMapper;

    @InjectMocks
    private AnimalService animalService;

    @Test
    @DisplayName("동물/품종 목록 조회 성공")
    void getAnimalsSuccessTest() {
        // given
        given(animalRepository.findAllWithAnimalKinds())
            .willReturn(List.of(mock(Animal.class)));
        given(animalMapper.toAnimalReadResult(anyList())).willReturn(List.of(mock(AnimalReadResults.Animal.class)));
        given(animalMapper.toAnimalReadResults(anyList())).willReturn(mock(AnimalReadResults.class));

        // when
        animalService.getAnimals();

        // then
        verify(animalRepository, times(1)).findAllWithAnimalKinds();
    }

}
