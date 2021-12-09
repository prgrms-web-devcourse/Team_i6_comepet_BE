package com.pet.domains.post.dto.response;

import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;


@Getter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class ShelterApiPageResult {

    @XmlElement(name = "body")
    private ShelterApiPageResult.Body body;

    @Getter
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Body {

        @XmlElement(name = "numOfRows")
        private Long numOfRows;

        @XmlElement(name = "pageNo")
        private Long pageNo;

        @XmlElement(name = "totalCount")
        private Long totalCount;

        @XmlElement(name = "items")
        private ShelterPostCreateParams items;
    }

}
