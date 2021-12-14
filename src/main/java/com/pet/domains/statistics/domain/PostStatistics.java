package com.pet.domains.statistics.domain;

import com.pet.domains.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_statistics")
public class PostStatistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "missing", columnDefinition = "BIGINT default 0", nullable = false)
    private long missing;

    @Column(name = "detection", columnDefinition = "BIGINT default 0", nullable = false)
    private long detection;

    @Column(name = "protection", columnDefinition = "BIGINT default 0", nullable = false)
    private long protection;

    @Column(name = "completion", columnDefinition = "BIGINT default 0", nullable = false)
    private long completion;

    @Column(name = "date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Builder
    public PostStatistics(long missing, long detection, long protection, long completion, LocalDateTime date) {
        this.missing = missing;
        this.detection = detection;
        this.protection = protection;
        this.completion = completion;
        this.date = date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("missing", missing)
            .append("detection", detection)
            .append("protection", protection)
            .append("completion", completion)
            .append("date", date)
            .toString();
    }
}
