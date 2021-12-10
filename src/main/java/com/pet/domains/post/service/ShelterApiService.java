package com.pet.domains.post.service;

import com.pet.common.property.ShelterProperties;
import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import com.pet.domains.animal.dto.response.AnimalKindApiPageResults;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.dto.request.CityCreateParams;
import com.pet.domains.area.dto.request.CityCreateParams.City;
import com.pet.domains.area.dto.request.TownCreateParams;
import com.pet.domains.area.dto.response.CityApiPageResults;
import com.pet.domains.area.dto.response.TownApiPageResults;
import com.pet.domains.area.service.CityService;
import com.pet.domains.area.service.TownService;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterApiPageResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShelterApiService {

    private static final String SHELTER_POST_PATH = "/abandonmentPublic";

    private static final String ANIMAL_KIND_PATH = "/kind";

    private static final String CITY_PATH = "/sido";

    private static final String TOWN_PATH = "/sigungu";

    private static final List<String> animalKindCodes = List.of("417000", "422400", "429900");

    private static final int NUM_OF_ROWS = 100;

    private final ShelterProperties shelterProperties;

    private final WebClient.Builder webClientBuilder;

    private final AnimalKindService animalKindService;

    private final CityService cityService;

    private final TownService townService;

    private WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(getBaseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        this.webClient = webClientBuilder.uriBuilderFactory(factory).baseUrl(getBaseUrl()).build();
    }

    public List<ShelterPostCreateParams> getShelterApiPageResultsBySync(LocalDate candidateForStart) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate start = Optional.ofNullable(candidateForStart).orElseGet(() -> yesterday);
        if (start.isAfter(yesterday)) {
            return Collections.emptyList();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String from = start.format(formatter);
        String to = yesterday.format(formatter);

        List<ShelterPostCreateParams> shelterPostCreateParams = new ArrayList<>();
        RequestHeadersSpec<?> requestHeadersSpec = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(SHELTER_POST_PATH)
                .queryParam("serviceKey", getApiKey())
                .queryParam("bgnde", from)
                .queryParam("endde", to)
                .build())
            .accept(MediaType.APPLICATION_XML);

        requestHeadersSpec.retrieve()
            .bodyToMono(ShelterApiPageResult.class)
            .block();

        // TODO 모든 페이지 call해서 가져오기

        return shelterPostCreateParams;
    }

    public void saveAllAnimalKinds() {
        Map<String, AnimalKindCreateParams> createParams = getAllAnimalKindCreateParams();
        createParams.forEach(animalKindService::createAnimalKinds);
    }

    public Map<String, AnimalKindCreateParams> getAllAnimalKindCreateParams() {
        return animalKindCodes.stream()
            .collect(Collectors.toMap(
                kindCode -> kindCode,
                kindCode -> getAnimalKindApiResults(kindCode).getBodyItems())
            );
    }

    public AnimalKindApiPageResults getAnimalKindApiResults(String kind) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(ANIMAL_KIND_PATH)
                .queryParam("serviceKey", getApiKey())
                .queryParam("up_kind_cd", kind)
                .build())
            .accept(MediaType.APPLICATION_XML)
            .retrieve()
            .bodyToMono(AnimalKindApiPageResults.class)
            .block();
    }

    // 매년, 12/11 06시, dev rds에 migrate 후 삭제
    @Scheduled(cron = "0 0 6 11 12 ?")
    public void saveAllCities() {
        log.info("saveAllCities() cron task start");
        CityCreateParams createParams = getCityCreateParams();
        cityService.createCites(createParams);
    }

    public CityCreateParams getCityCreateParams() {
        return getCityApiPageResults().getBodyItems();
    }


    public CityApiPageResults getCityApiPageResults() {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(CITY_PATH)
                .queryParam("serviceKey", getApiKey())
                .queryParam("numOfRows", NUM_OF_ROWS)
                .build())
            .accept(MediaType.APPLICATION_XML)
            .retrieve()
            .bodyToMono(CityApiPageResults.class)
            .block();
    }

    // 매년, 12/11 06시 10, dev rds에 migrate 후 삭제
    @Scheduled(cron = "0 10 6 11 12 ?")
    public void saveAllTowns() {
        log.info("saveAllTowns() cron task start");
        Map<String, TownCreateParams> createParams = getAllTownCreateParams();
        createParams.forEach(townService::createTowns);
    }

    public Map<String, TownCreateParams> getAllTownCreateParams() {
        Map<String, TownCreateParams> paramsMap = new HashMap<>();
        getCityCodes().forEach(cityCode -> {
            TownCreateParams createParams = getTownApiPageResults(cityCode).getBodyItems();
            if (createParams.getTowns() != null) {
                paramsMap.put(cityCode, createParams);
            }
        });
        return paramsMap;
    }

    public Set<String> getCityCodes() {
        CityCreateParams bodyItems = getCityApiPageResults().getBodyItems();
        return bodyItems.getCities().stream()
            .map(City::getCode)
            .collect(Collectors.toSet());
    }

    public TownApiPageResults getTownApiPageResults(String cityCode) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(TOWN_PATH)
                .queryParam("serviceKey", getApiKey())
                .queryParam("upr_cd", cityCode)
                .build())
            .accept(MediaType.APPLICATION_XML)
            .retrieve()
            .bodyToMono(TownApiPageResults.class)
            .block();
    }

    private String getBaseUrl() {
        return shelterProperties.getUrl();
    }

    private String getApiKey() {
        return shelterProperties.getKey();
    }
}
