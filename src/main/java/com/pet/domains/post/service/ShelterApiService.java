package com.pet.domains.post.service;

import com.pet.common.property.ShelterProperties;
import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import com.pet.domains.animal.dto.response.AnimalKindApiPageResults;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterApiPageResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    private static final List<String> animalKindCodes = List.of("417000", "422400", "429900");

    private final ShelterProperties shelterProperties;

    private final WebClient.Builder webClientBuilder;

    private final AnimalKindService animalKindService;


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

    public void saveAnimalKinds() {
        Map<String, AnimalKindCreateParams> createParams = getAnimalKindCreateParams();
        createParams.forEach(animalKindService::createAnimalKinds);
    }

    public Map<String, AnimalKindCreateParams> getAnimalKindCreateParams() {

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

    private String getBaseUrl() {
        return shelterProperties.getUrl();
    }

    private String getApiKey() {
        return shelterProperties.getKey();
    }
}
