package com.pet.domains.statistics.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.statistics.dto.response.PostStatisticsReadResult;
import com.pet.domains.statistics.service.PostStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@RestController
public class PostStatisticsController {

    private final PostStatisticsService postStatisticsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PostStatisticsReadResult> getPostStatistics() {
        return ApiResponse.ok(postStatisticsService.getPostStatistics());
    }

}
