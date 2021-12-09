package com.pet.domains.post.domain;

import com.pet.domains.EnumType;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum SexType implements EnumType {
    MALE("수컷", "M"),
    FEMALE("암컷", "F"),
    UNKNOWN("모름", "Q");

    private static final Map<String, SexType> sexTypeByAbbr = Stream.of(SexType.values())
        .collect(Collectors.toUnmodifiableMap(SexType::getAbbreviation, value -> value));


    private final String text;

    private final String abbreviation;

    SexType(String text, String abbreviation) {
        this.text = text;
        this.abbreviation = abbreviation;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getText() {
        return text;
    }


    public static SexType findSexType(String abbreviation) {
        return Optional.ofNullable(sexTypeByAbbr.get(abbreviation))
            .orElseThrow(() -> new IllegalArgumentException("No match sex type"));
    }

}
