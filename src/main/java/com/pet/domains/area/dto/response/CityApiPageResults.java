package com.pet.domains.area.dto.response;

import com.pet.domains.area.dto.request.CityCreateParams;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class CityApiPageResults {

    @XmlElement(name = "body")
    private CityApiPageResults.Body body;

    @Getter
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Body {

        @XmlElement(name = "items")
        private CityCreateParams items;
    }

    public CityCreateParams getBodyItems() {
        Objects.requireNonNull(body, "시도 조회 api 응답 바디가 널입니다.");
        return body.getItems();
    }

}
