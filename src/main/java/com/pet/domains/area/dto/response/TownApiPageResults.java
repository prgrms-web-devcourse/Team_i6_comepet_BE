package com.pet.domains.area.dto.response;

import com.pet.domains.area.dto.request.TownCreateParams;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class TownApiPageResults {
    @XmlElement(name = "body")
    private TownApiPageResults.Body body;

    @Getter
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Body {

        @XmlElement(name = "items")
        private TownCreateParams items;

    }

    public TownCreateParams getBodyItems() {
        return body.getItems();
    }

}
