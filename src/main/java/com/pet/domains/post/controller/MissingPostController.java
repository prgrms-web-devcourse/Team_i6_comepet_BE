package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class MissingPostController {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
        path = "/missing-posts",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Map<String, Long>> createMissingPost(@RequestBody MissingPostCreateParam param) {
        return ApiResponse.ok(Map.of("id", 1L));
    }

}
