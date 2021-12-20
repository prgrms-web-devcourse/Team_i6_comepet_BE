package com.pet.domains.post.dto.request;


import com.pet.domains.post.domain.NeuteredType;
import com.pet.domains.post.domain.SexType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.NONE)
public class ShelterPostCreateParams {

    private static final String UNKNOWN = "모름";

    private static final String ALL = "전체";

    @XmlElement(name = "item")
    private List<ShelterPostCreateParams.ShelterPost> shelterPosts;

    @Getter
    @XmlRootElement(name = "item")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class ShelterPost {

        @XmlJavaTypeAdapter(AgeAdapter.class)
        @XmlElement(name = "age")
        private Long age;

        @XmlElement(name = "careAddr")
        private String shelterPlace;

        @XmlElement(name = "careNm")
        private String shelterName;

        @XmlElement(name = "careTel")
        private String shelterTelNumber;

        @XmlElement(name = "chargeNm")
        private String manager;

        @XmlElement(name = "colorCd")
        private String color;

        @XmlElement(name = "desertionNo")
        private String desertionNumber;

        @XmlElement(name = "filename")
        private String thumbnail;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "happenDt")
        private LocalDate foundDate;

        @XmlElement(name = "happenPlace")
        private String foundPlace;

        @XmlElement(name = "kindCd")
        private String kindCd;

        @XmlJavaTypeAdapter(NeuteredTypeAdapter.class)
        @XmlElement(name = "neuterYn")
        private NeuteredType neutered;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "noticeEdt")
        private LocalDate endDate;

        @XmlElement(name = "noticeNo")
        private String noticeNumber;

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        @XmlElement(name = "noticeSdt")
        private LocalDate startDate;

        @XmlElement(name = "officetel")
        private String managerTelNumber;

        @XmlElement(name = "orgNm")
        private String address;

        @XmlElement(name = "popfile")
        private String image;

        @XmlElement(name = "processState")
        private String postStatus;

        @XmlJavaTypeAdapter(SexTypeAdapter.class)
        @XmlElement(name = "sexCd")
        private SexType sex;

        @XmlElement(name = "specialMark")
        private String feature;

        @XmlJavaTypeAdapter(DoubleAdapter.class)
        @XmlElement(name = "weight")
        private Double weight;

        public String getAnimalKindNameFromKindCd() {
            // format: [{동물}] {품종}, ex) [고양이] 한국 고양이
            ObjectUtils.requireNonEmpty(kindCd, "kindCd data must not be null");

            int whileSpaceIdx = StringUtils.indexOf(kindCd, " ");
            if (whileSpaceIdx == -1) {
                return UNKNOWN;
            }
            String splitName = StringUtils.substring(kindCd, whileSpaceIdx + 1);
            if (StringUtils.isEmpty(splitName)) {
                return UNKNOWN;
            }
            return splitName.strip();
        }

        public String getCityNameFromAddress() {
            ObjectUtils.requireNonEmpty(address, "address data must not be null");

            return StringUtils.split(address)[0];
        }

        public String getTownNameFromAddress() {
            String[] split = StringUtils.split(address);
            int size = split.length;
            if (size == 1) {
                return ALL;
            }
            return split[size - 1];
        }

        public static class AgeAdapter extends XmlAdapter<String, Long> {

            @Override
            public Long unmarshal(String value) {
                // format: {년도}(년생), ex) 2021(년생)
                String extractedAge = StringUtils.substringBefore(value, "(");
                return Long.valueOf(extractedAge);
            }

            @Override
            public String marshal(Long value) {
                return String.valueOf(value);
            }
        }

        public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

            @Override
            public LocalDate unmarshal(String value) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                return LocalDate.parse(value, formatter);
            }

            @Override
            public String marshal(LocalDate value) {
                return value.toString();
            }
        }

        public static class NeuteredTypeAdapter extends XmlAdapter<String, NeuteredType> {

            @Override
            public NeuteredType unmarshal(String value) throws IllegalArgumentException {
                return NeuteredType.valueOf(value);
            }

            @Override
            public String marshal(NeuteredType value) {
                return value.name();
            }
        }

        public static class SexTypeAdapter extends XmlAdapter<String, SexType> {

            @Override
            public SexType unmarshal(String value) {
                return SexType.findSexType(value);
            }

            @Override
            public String marshal(SexType value) {
                return value.getAbbreviation();
            }
        }

        public static class DoubleAdapter extends XmlAdapter<String, Double> {

            @Override
            public Double unmarshal(String value) {
                // format: {몸무게}(kg), ex) 20.00(kg)
                String extractedWeight = StringUtils.substringBefore(value, "(");
                return Double.valueOf(extractedWeight);
            }

            @Override
            public String marshal(Double value) {
                return value.toString();
            }
        }
    }
}
