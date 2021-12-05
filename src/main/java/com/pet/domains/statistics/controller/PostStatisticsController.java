package com.pet.domains.statistics.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.statistics.dto.response.PostStatisticsReadResult;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/statistics")
@RestController
public class PostStatisticsController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PostStatisticsReadResult> getPostStatistics() {
        return ApiResponse.ok(PostStatisticsReadResult.of(124L, 53L, 27L, 564L, LocalDateTime.now()));
    }

}
