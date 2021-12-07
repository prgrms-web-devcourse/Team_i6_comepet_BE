package com.pet.domains.docs.controller;


import com.pet.common.response.ApiResponse;
import com.pet.domains.EnumType;
import com.pet.domains.docs.dto.CommonDocumentationResults;
import com.pet.domains.post.domain.NeuteredType;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.ShelterSexType;
import com.pet.domains.post.domain.Status;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonDocumentationController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/common")
    public ApiResponse<CommonDocumentationResults> findAll() {

        Map<String, String> sexTypes = getDocs(SexType.values());
        Map<String, String> shelterSexTypes = getDocs(ShelterSexType.values());
        Map<String, String> neuteredTypes = getDocs(NeuteredType.values());
        Map<String, String> status = getDocs(Status.values());

        return ApiResponse.ok(
            CommonDocumentationResults.builder()
                .sexTypes(sexTypes)
                .shelterSexTypes(shelterSexTypes)
                .neuteredTypes(neuteredTypes)
                .status(status)
                .build()
        );
    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .collect(Collectors.toMap(EnumType::getName, EnumType::getText));
    }

}
