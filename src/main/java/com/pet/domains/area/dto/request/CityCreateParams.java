package com.pet.domains.area.dto.request;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.NONE)
public class CityCreateParams {

    @XmlElement(name = "item")
    private List<CityCreateParams.City> cities;

    @Getter
    @XmlRootElement(name = "item")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class City {

        @XmlElement(name = "orgCd")
        private String code;

        @XmlElement(name = "orgdownNm")
        private String name;
    }

}
