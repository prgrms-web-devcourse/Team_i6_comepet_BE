package com.pet.domains.animal.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
@RestController
public class AnimalController {

    private final AnimalService animalService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<AnimalReadResults> getAnimals() {
        return ApiResponse.ok(animalService.getAnimals());
    }

}
