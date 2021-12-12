package com.pet.domains.animal.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.repository.AnimalKindRepository;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("동물 컨트롤러 통합 테스트")
class AnimalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalKindRepository animalKindRepository;

    @Test
    @DisplayName("동물/품종 조회 성공 테스트")
    void getAnimalsTest() throws Exception {
        // given
        Animal animal = animalRepository.save(
            Animal.builder()
                .code("1234")
                .name("testAnimal")
                .build()
        );
        animalKindRepository.saveAll(
            IntStream.rangeClosed(1, 5)
                .mapToObj(id -> AnimalKind.builder()
                    .name("name#" + id)
                    .animal(animal)
                    .build())
                .collect(Collectors.toList())
        );

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/animals")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(handler().handlerType(AnimalController.class))
            .andExpect(handler().methodName("getAnimals"))
            .andExpect(jsonPath("$.data", is(notNullValue())))
            .andExpect(jsonPath("$.data.animals", hasSize(1)))
            .andExpect(jsonPath("$.data.animals[0].id", is(1)))
            .andExpect(jsonPath("$.data.animals[0].name", is("testAnimal")))
            .andExpect(jsonPath("$.data.animals[0].kinds", hasSize(5)))
            .andExpect(jsonPath("$.data.animals[0].kinds[0].id", is(1)))
            .andExpect(jsonPath("$.data.animals[0].kinds[0].name", is("name#1")))
            .andExpect(jsonPath("$.serverDateTime", is(notNullValue())));
    }
}
