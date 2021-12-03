package com.pet.domains.animal.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/animals")
@RestController
public class AnimalController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AnimalReadResults> getAnimals() {

        return ApiResponse.ok(
            AnimalReadResults.of(List.of(
                AnimalReadResults.Animal.of(1L, "개", List.of(
                    AnimalReadResults.Animal.AnimalKind.of(1L, "골든 리트리버"),
                    AnimalReadResults.Animal.AnimalKind.of(2L, "그레이 하운드"),
                    AnimalReadResults.Animal.AnimalKind.of(3L, "골든 리트리버"),
                    AnimalReadResults.Animal.AnimalKind.of(4L, "그레이트 덴"),
                    AnimalReadResults.Animal.AnimalKind.of(5L, "네오폴리탄 마스티프"),
                    AnimalReadResults.Animal.AnimalKind.of(6L, "노르포크 테리어"),
                    AnimalReadResults.Animal.AnimalKind.of(7L, "닥스훈트"),
                    AnimalReadResults.Animal.AnimalKind.of(8L, "달마시안")
                )),
                AnimalReadResults.Animal.of(2L, "고양이", List.of(
                    AnimalReadResults.Animal.AnimalKind.of(1L, "노르웨이 숲"),
                    AnimalReadResults.Animal.AnimalKind.of(2L, "데본 렉스"),
                    AnimalReadResults.Animal.AnimalKind.of(3L, "러시안 블루"),
                    AnimalReadResults.Animal.AnimalKind.of(4L, "레그돌"),
                    AnimalReadResults.Animal.AnimalKind.of(5L, "맹크스"),
                    AnimalReadResults.Animal.AnimalKind.of(6L, "먼치킨"),
                    AnimalReadResults.Animal.AnimalKind.of(7L, "믹스묘"),
                    AnimalReadResults.Animal.AnimalKind.of(8L, "발리네즈")
                )),
                AnimalReadResults.Animal.of(3L, "기타", List.of(
                    AnimalReadResults.Animal.AnimalKind.of(1L, "기타축종"),
                    AnimalReadResults.Animal.AnimalKind.of(2L, "거북이"),
                    AnimalReadResults.Animal.AnimalKind.of(3L, "다람쥐"),
                    AnimalReadResults.Animal.AnimalKind.of(4L, "물고기"),
                    AnimalReadResults.Animal.AnimalKind.of(5L, "도마뱀"),
                    AnimalReadResults.Animal.AnimalKind.of(6L, "이구아나"),
                    AnimalReadResults.Animal.AnimalKind.of(7L, "햄스터")
                ))
            )));
    }
}
