package com.pet.domains.docs.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonDocumentationResults {

    private final Map<String, String> sexTypes;
    private final Map<String, String> shelterSexTypes;
    private final Map<String, String> neuteredTypes;
    private final Map<String, String> status;

    @Builder
    public CommonDocumentationResults(
        Map<String, String> sexTypes,
        Map<String, String> shelterSexTypes,
        Map<String, String> neuteredTypes,
        Map<String, String> status
    ) {
        this.sexTypes = sexTypes;
        this.shelterSexTypes = shelterSexTypes;
        this.neuteredTypes = neuteredTypes;
        this.status = status;
    }
}
