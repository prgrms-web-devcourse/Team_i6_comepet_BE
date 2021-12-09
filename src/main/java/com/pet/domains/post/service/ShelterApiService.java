package com.pet.domains.post.service;

import com.pet.common.property.ShelterProperties;
import com.pet.common.property.ShelterProperties.Api;
import com.pet.domains.post.dto.response.ShelterApiPageResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RequiredArgsConstructor
@Service
public class ShelterApiService {

    private static final String SHELTER_POSH_PATH = "/abandonmentPublic";

    private final ShelterProperties shelterProperties;

    private final WebClient.Builder webClientBuilder;

    public List<ShelterApiPageResult> getShelterApiPageResults(LocalDate candidateForStart) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate start = Optional.ofNullable(candidateForStart).orElseGet(() -> yesterday);
        if (start.isAfter(yesterday)) {
            return Collections.emptyList();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String from = start.format(formatter);
        String to = yesterday.format(formatter);

        List<ShelterApiPageResult> results = new ArrayList<>();
        Api api = shelterProperties.getApi();
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(api.getUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = webClientBuilder.uriBuilderFactory(factory).baseUrl(api.getUrl()).build();
        webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(SHELTER_POSH_PATH)
                .queryParam("serviceKey", api.getKey())
                .queryParam("bgnde", from)
                .queryParam("endde", to)
                .build())
            .accept(MediaType.APPLICATION_XML)
            .retrieve()
            .bodyToMono(ShelterApiPageResult.class)
            .subscribe(results::add);

        return results;
    }

}
