package com.pet.domains.animal.dto.request;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;


@Getter
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.NONE)
public class AnimalKindCreateParams {

    @XmlElement(name = "item")
    private List<AnimalKindCreateParams.AnimalKind> animalKinds;

    @Getter
    @XmlRootElement(name = "item")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class AnimalKind {

        @XmlElement(name = "KNm")
        private String name;

        @XmlElement(name = "kindCd")
        private String code;
    }
}
