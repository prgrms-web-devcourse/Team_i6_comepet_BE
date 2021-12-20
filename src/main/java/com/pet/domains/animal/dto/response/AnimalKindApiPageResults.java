package com.pet.domains.animal.dto.response;

import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class AnimalKindApiPageResults {

    @XmlElement(name = "body")
    private AnimalKindApiPageResults.Body body;

    @Getter
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Body {

        @XmlElement(name = "items")
        private AnimalKindCreateParams items;

    }

    public AnimalKindCreateParams getBodyItems() {
        Objects.requireNonNull(body, "품종 조회 api 응답 바디가 널입니다.");
        return body.getItems();
    }
}
