package com.pet.domains.area.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.area.dto.CityReadResults;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CityReadResults> getCities() {
        return ApiResponse.ok(CityReadResults.of(
            List.of(
                CityReadResults.City.of(1L, "서울특별시", List.of(
                    CityReadResults.City.Town.of(1L, "강남구"),
                    CityReadResults.City.Town.of(2L, "강동구"),
                    CityReadResults.City.Town.of(3L, "강서구"),
                    CityReadResults.City.Town.of(4L, "관악구"),
                    CityReadResults.City.Town.of(5L, "광진구"),
                    CityReadResults.City.Town.of(6L, "구로구"),
                    CityReadResults.City.Town.of(7L, "금천구"),
                    CityReadResults.City.Town.of(8L, "노원구"),
                    CityReadResults.City.Town.of(9L, "동대문구"),
                    CityReadResults.City.Town.of(10L, "동작구"),
                    CityReadResults.City.Town.of(11L, "서초구"),
                    CityReadResults.City.Town.of(12L, "성동구"),
                    CityReadResults.City.Town.of(13L, "성북구"),
                    CityReadResults.City.Town.of(14L, "송파구"),
                    CityReadResults.City.Town.of(15L, "양천구"),
                    CityReadResults.City.Town.of(16L, "영등포구"),
                    CityReadResults.City.Town.of(17L, "용산구"),
                    CityReadResults.City.Town.of(18L, "은평구"),
                    CityReadResults.City.Town.of(19L, "종로구"),
                    CityReadResults.City.Town.of(20L, "중구"),
                    CityReadResults.City.Town.of(21L, "중랑구")
                )),
                CityReadResults.City.of(2L, "부산광역시", List.of(
                    CityReadResults.City.Town.of(22L, "중구"),
                    CityReadResults.City.Town.of(23L, "서구"),
                    CityReadResults.City.Town.of(24L, "동구"),
                    CityReadResults.City.Town.of(25L, "영도구"),
                    CityReadResults.City.Town.of(26L, "부산진구"),
                    CityReadResults.City.Town.of(27L, "동래구"),
                    CityReadResults.City.Town.of(28L, "남구"),
                    CityReadResults.City.Town.of(29L, "해운대구"),
                    CityReadResults.City.Town.of(30L, "사하구"),
                    CityReadResults.City.Town.of(31L, "금정구"),
                    CityReadResults.City.Town.of(32L, "강서구"),
                    CityReadResults.City.Town.of(33L, "연제구"),
                    CityReadResults.City.Town.of(34L, "수영구"),
                    CityReadResults.City.Town.of(35L, "사상구"),
                    CityReadResults.City.Town.of(36L, "기장군")
                )),
                CityReadResults.City.of(3L, "대구광역시", List.of(
                    CityReadResults.City.Town.of(37L, "북구"),
                    CityReadResults.City.Town.of(38L, "동구"),
                    CityReadResults.City.Town.of(40L, "중구"),
                    CityReadResults.City.Town.of(41L, "서구"),
                    CityReadResults.City.Town.of(42L, "달서구"),
                    CityReadResults.City.Town.of(43L, "남구"),
                    CityReadResults.City.Town.of(44L, "달성군")
                ))
            )
        ));
    }

}
